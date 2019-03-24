package com.afrunt.scimdb.crawler;

import com.afrunt.scimdb.crawler.routes.CrawlerRoutes;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrii Frunt
 */
@RestController
public class CrawlerController {
    @Autowired
    private CrawlerRoutes crawlerRoutes;

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping("/start")
    public void start() {
        producerTemplate.asyncSendBody("direct:START_CRAWLING", "START");
    }

    @GetMapping("/stop")
    public void stop() {
        producerTemplate.asyncSendBody("direct:STOP_CRAWLING", "STOP");
    }
}
