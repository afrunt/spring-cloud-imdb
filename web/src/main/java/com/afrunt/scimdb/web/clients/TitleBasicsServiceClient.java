package com.afrunt.scimdb.web.clients;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponse;
import com.afrunt.scimdb.dto.titlebasics.TitleBasicsSearchRequest;
import com.afrunt.scimdb.dto.titlebasics.TitleBasicsStatistics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * @author Andrii Frunt
 */
@Primary
@FeignClient(name = "title-basics-service", decode404 = true, fallback = TitleBasicsServiceClient.TitleBasicsServiceClientFallback.class)
public interface TitleBasicsServiceClient {

    @GetMapping(value = "/genres/withTitles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, Long>> findGenresWithTitleCount();

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TitleBasics> titleById(@PathVariable("id") Long id);

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<TitleBasics>> search(@RequestBody TitleBasicsSearchRequest titleBasicsSearchRequest);

    @GetMapping("/stats")
    ResponseEntity<TitleBasicsStatistics> stats();

    @DeleteMapping("/deleteAll")
    ResponseEntity<Boolean> deleteAll();

    @Component
    class TitleBasicsServiceClientFallback implements TitleBasicsServiceClient {

        @Override
        public ResponseEntity<Map<String, Long>> findGenresWithTitleCount() {
            return ResponseEntity.ok(
                    Collections.emptyMap()
            );
        }

        @Override
        public ResponseEntity<TitleBasics> titleById(Long id) {
            return ResponseEntity
                    .status(500)
                    .build();
        }

        @Override
        public ResponseEntity<PageResponse<TitleBasics>> search(TitleBasicsSearchRequest titleBasicsSearchRequest) {
            return ResponseEntity
                    .ok(new PageResponse<>());
        }

        @Override
        public ResponseEntity<TitleBasicsStatistics> stats() {
            return ResponseEntity
                    .ok(
                            new TitleBasicsStatistics()
                                    .setAvailable(false)
                                    .setGenres(0)
                                    .setTitles(0)
                    );
        }

        @Override
        public ResponseEntity<Boolean> deleteAll() {
            return ResponseEntity
                    .ok(false);
        }
    }

}
