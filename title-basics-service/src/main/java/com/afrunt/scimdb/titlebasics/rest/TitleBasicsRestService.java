package com.afrunt.scimdb.titlebasics.rest;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponse;
import com.afrunt.scimdb.dto.titlebasics.SearchRequest;
import com.afrunt.scimdb.titlebasics.TitleNotFoundException;
import com.afrunt.scimdb.titlebasics.service.TitleBasicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

/**
 * @author Andrii Frunt
 */
@RestController
public class TitleBasicsRestService {

    @Autowired
    private TitleBasicsService titleBasicsService;

    @GetMapping("/clear")
    public ResponseEntity<Boolean> clear() {
        titleBasicsService.deleteAll();
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> stats() {
        StopWatch sw = new StopWatch();
        sw.start();
        long countGenres = titleBasicsService.allGenresCount();
        long count = titleBasicsService.count();
        sw.stop();


        return ResponseEntity.ok(Map.ofEntries(
                entry("titles", count),
                entry("genres", countGenres),
                entry("took", sw.getTotalTimeMillis())
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TitleBasics> titleById(@PathVariable("id") Long id) {
        return titleBasicsService
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/bulk", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createTitles(@RequestBody List<TitleBasics> titlesBasics) {
        StopWatch sw = new StopWatch();
        sw.start();
        long countOfCreated = titleBasicsService.createTitlesBasics(titlesBasics).size();
        sw.stop();
        return ResponseEntity.ok(Map.ofEntries(
                entry("success", true),
                entry("submitted", titlesBasics.size()),
                entry("created", countOfCreated),
                entry("elapsed", sw.getTotalTimeMillis())
        ));
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<TitleBasics>> search(@RequestBody SearchRequest searchRequest) {

        Page<TitleBasics> page = titleBasicsService.search(searchRequest);

        return ResponseEntity.ok(new PageResponse<TitleBasics>()
                .setPage(page.getNumber())
                .setPerPage(page.getSize())
                .setPages(page.getTotalPages())
                .setTotal(page.getTotalElements())
                .setItems(page.getContent())
        );
    }

    @GetMapping(value = "/genres/withTitles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> findGenresWithTitleCount() {
        return ResponseEntity.ok(titleBasicsService.findGenresWithCountOfTitles());
    }

    @GetMapping(value = "/genres/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getAllGenres() {
        return ResponseEntity.ok(titleBasicsService.allGenres());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = TitleNotFoundException.class)
    public Map<String, Object> titleNotFoundExceptionHandler(TitleNotFoundException e) {
        return Map.ofEntries(
                entry("error", e.getMessage())
        );
    }
}
