package com.afrunt.scimdb.crawler;

import com.afrunt.scimdb.crawler.dto.CrawlerStatistics;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrii Frunt
 */
@RestController
public class CrawlerController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CrawlerStatistics> stats() {
        return ResponseEntity.ok(
                new CrawlerStatistics()
                        .setRunning(producerTemplate.requestBody("direct:isRunning", "", Boolean.class))
        );
    }

    @PostMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> start() {
        producerTemplate.asyncSendBody("direct:START_CRAWLING", "START");
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> stop() {
        producerTemplate.asyncSendBody("direct:STOP_CRAWLING", "STOP");
        return ResponseEntity.ok(true);
    }
}
