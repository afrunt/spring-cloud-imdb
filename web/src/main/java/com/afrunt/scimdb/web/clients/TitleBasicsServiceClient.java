package com.afrunt.scimdb.web.clients;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponse;
import com.afrunt.scimdb.dto.titlebasics.TitleBasicsSearchRequest;
import com.afrunt.scimdb.dto.titlebasics.TitleBasicsStatistics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Andrii Frunt
 */
@Primary
@FeignClient(name = "title-basics-service", decode404 = true)
public interface TitleBasicsServiceClient {

    @GetMapping(value = "/genres/withTitles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, Long>> findGenresWithTitleCount();

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TitleBasics> titleById(@PathVariable("id") Long id);

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponse<TitleBasics>> search(@RequestBody TitleBasicsSearchRequest titleBasicsSearchRequest);

    @GetMapping("/stats")
    ResponseEntity<TitleBasicsStatistics> stats();
}
