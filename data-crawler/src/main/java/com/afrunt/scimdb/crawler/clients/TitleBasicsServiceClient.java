package com.afrunt.scimdb.crawler.clients;

import com.afrunt.imdb.model.TitleBasics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Andrii Frunt
 */
@Primary
@FeignClient(name = "title-basics-service", fallback = TitleBasicsServiceClient.TitleBasicsServiceClientFallback.class)
public interface TitleBasicsServiceClient {
    @PostMapping(value = "/bulk", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, Object>> createTitlesBasics(List<TitleBasics> titleBasics);

    @Component
    class TitleBasicsServiceClientFallback implements TitleBasicsServiceClient {
        @Override
        public ResponseEntity<Map<String, Object>> createTitlesBasics(List<TitleBasics> titleBasics) {
            return ResponseEntity.ok(Map.ofEntries(
                    entry("success", false),
                    entry("submitted", titleBasics.size()),
                    entry("created", 0)
            ));
        }
    }
}
