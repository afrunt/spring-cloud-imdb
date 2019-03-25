package com.afrunt.scimdb.web.clients;

import com.afrunt.scimdb.dto.crawler.CrawlerStatistics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Andrii Frunt
 */
@Primary
@FeignClient(name = "crawler-service", fallback = CrawlerServiceClient.CrawlerServiceClientFallback.class)
public interface CrawlerServiceClient {
    @GetMapping("/stats")
    ResponseEntity<CrawlerStatistics> stats();

    @PostMapping("/start")
    ResponseEntity<Boolean> start();

    @PostMapping("/stop")
    ResponseEntity<Boolean> stop();

    @Component
    class CrawlerServiceClientFallback implements CrawlerServiceClient {

        @Override
        public ResponseEntity<CrawlerStatistics> stats() {
            return ResponseEntity.ok(
                    new CrawlerStatistics()
                            .setAvailable(false)
                            .setRunning(false)
            );
        }

        @Override
        public ResponseEntity<Boolean> start() {
            return ResponseEntity.ok(false);
        }

        @Override
        public ResponseEntity<Boolean> stop() {
            return ResponseEntity.ok(false);
        }
    }
}
