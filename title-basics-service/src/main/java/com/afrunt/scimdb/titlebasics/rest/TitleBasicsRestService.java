package com.afrunt.scimdb.titlebasics.rest;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponseDto;
import com.afrunt.scimdb.titlebasics.dto.TitleBasicsSearchRequest;
import com.afrunt.scimdb.titlebasics.dto.TitleBasicsStatistics;
import com.afrunt.scimdb.titlebasics.TitleNotFoundException;
import com.afrunt.scimdb.titlebasics.service.TitleBasicsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TitleBasicsRestService.class);

    @Autowired
    private TitleBasicsService titleBasicsService;

    @DeleteMapping("/clear")
    public ResponseEntity<Boolean> clear() {
        titleBasicsService.deleteAll();
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TitleBasicsStatistics> stats() {
        StopWatch sw = new StopWatch();
        sw.start();
        long countOfTitles = titleBasicsService.count();
        long countOfGenres = titleBasicsService.allGenresCount();
        sw.stop();

        LOGGER.debug("Title Basics statistics collected in {}ms", sw.getTotalTimeMillis());

        return ResponseEntity.ok(new TitleBasicsStatistics()
                .setTitles(countOfTitles)
                .setGenres(countOfGenres)
        );
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
    public ResponseEntity<PageResponseDto<TitleBasics>> search(@RequestBody TitleBasicsSearchRequest titleBasicsSearchRequest) {

        Page<TitleBasics> page = titleBasicsService.search(titleBasicsSearchRequest);

        return ResponseEntity.ok(new PageResponseDto<TitleBasics>()
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

    @GetMapping(value = "/genres/with-titles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> findGenresWithTitleCount2() {
        return ResponseEntity.ok(titleBasicsService.findGenresWithCountOfTitles());
    }

    @GetMapping(value = "/genres/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getAllGenres() {
        return ResponseEntity.ok(titleBasicsService.allGenres());
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Boolean> deleteAll() {
        titleBasicsService.deleteAll();
        return ResponseEntity.ok(true);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = TitleNotFoundException.class)
    public Map<String, Object> titleNotFoundExceptionHandler(TitleNotFoundException e) {
        return Map.ofEntries(
                entry("error", e.getMessage())
        );
    }
}
