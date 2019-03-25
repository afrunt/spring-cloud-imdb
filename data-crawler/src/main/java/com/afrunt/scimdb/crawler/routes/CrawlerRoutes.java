package com.afrunt.scimdb.crawler.routes;

import com.afrunt.imdb.client.IMDbClient;
import com.afrunt.scimdb.crawler.clients.TitleBasicsServiceClient;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Andrii Frunt
 */
@Component
public class CrawlerRoutes extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerRoutes.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private IMDbClient client;

    @Autowired
    private TitleBasicsServiceClient titleBasicsServiceClient;

    private boolean crawlingStopped = true;

    // @formatter:off
    @Override
    public void configure() throws Exception {
        onException(Throwable.class)
                .process(exchange -> crawlingStopped = true);

        from("scheduler:scheduledCrawling?initialDelay=36&delay=36&timeUnit=HOURS")
                .id("Crawling Scheduler")
                .routeId("Crawling Scheduler")
                .log("Scheduled crawling event")
                .to("direct:START_CRAWLING");

        from("direct:START_CRAWLING")
                .id("Crawling Starter")
                .routeId("Crawling Starter")
                .filter(exchange -> crawlingStopped)
                .log("Starting of crawling process")
                .to("direct:crawling");

        from("direct:STOP_CRAWLING")
                .id("Crawling Terminator")
                .routeId("Crawling Terminator")
                .filter(exchange -> !crawlingStopped)
                .log("Manual stopping of crawling process")
                .process(exchange -> crawlingStopped = true)
                .process(exchange ->  client.stopStreaming())
        ;


        from("direct:crawling")
                .id("Crawler Route")
                .routeId("Crawler Route")
                .log("Starting crawling")
                .filter(exchange -> crawlingStopped)
                .process(exchange -> crawlingStopped = false)
                .process(exchange -> streamTitleBasics())
                .process(exchange -> crawlingStopped = true)
                .log("Crawling finished");

        from("direct:titleBasics")
                .id("Title Basics Route")
                .routeId("Title Basics Route")
                .filter(exchange -> !crawlingStopped)
                .aggregate(constant(true), new GroupedBodyAggregationStrategy())
                .completionSize(10000)
                .completionInterval(TimeUnit.SECONDS.toMillis(10))
                .threads(5)
                .choice()
                    .when(exchange -> crawlingStopped)
                        .stop()
                .end()
                .to("direct:titleBasicsConsumer")
        ;

        from("direct:titleBasicsConsumer")
                .id("titleBasicsBatchConsumer")
                .routeId("titleBasicsBatchConsumer")
                .log("Title Basics Batch sent ${body.size()}")
                .bean(titleBasicsServiceClient, "createTitlesBasics")
                .log("Title Basics Batch processed \n${body.getBody()}");

        from("direct:isRunning")
                .setBody(() -> !crawlingStopped);
    }

    // @formatter:on

    public void streamTitleBasics() {
        if (!crawlingStopped) {
            client
                    .titleBasicsStream()
                    .filter(titleBasics -> !crawlingStopped)
                    .forEach(titleBasics -> producerTemplate.asyncSendBody("direct:titleBasics", titleBasics));
        }
    }
}
