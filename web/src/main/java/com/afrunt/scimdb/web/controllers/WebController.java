package com.afrunt.scimdb.web.controllers;

import com.afrunt.imdb.model.TitleBasics;
import com.afrunt.scimdb.dto.PageResponse;
import com.afrunt.scimdb.dto.titlebasics.TitleBasicsSearchRequest;
import com.afrunt.scimdb.web.clients.CrawlerServiceClient;
import com.afrunt.scimdb.web.clients.TitleBasicsServiceClient;
import com.afrunt.scimdb.web.exception.TitleNotFoundException;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Map.entry;

/**
 * @author Andrii Frunt
 */
@Controller
public class WebController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private TitleBasicsServiceClient titleBasicsServiceClient;

    @Autowired
    private CrawlerServiceClient crawlerServiceClient;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = {"", "/"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "term", required = false, defaultValue = "") String term,
            @RequestParam(name = "genre", required = false, defaultValue = "") String genre,
            @RequestParam(name = "startYear", required = false) Integer startYear,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "perPage", defaultValue = "20") int perPage,
            Model model) {

        StopWatch sw = new StopWatch();
        sw.start();

        PageResponse<TitleBasics> pageResponse = titleBasicsServiceClient.search(new TitleBasicsSearchRequest()
                .setTerm(term)
                .setGenres(StringUtils.isEmpty(genre) || "any".equalsIgnoreCase(genre) ? Collections.emptyList() : Collections.singletonList(genre))
                .setStartYear(startYear)
                .setPage(page)
                .setPerPage(perPage)
        ).getBody();

        sw.stop();

        LOGGER.info("Search results (page={}, perPage={}, itemsCount={}, pages={}, itemsTotal={}) fetched in {}ms",
                pageResponse.getPage(),
                pageResponse.getPerPage(),
                pageResponse.getItemsCount(),
                pageResponse.getPages(),
                pageResponse.getTotal(),
                sw.getTotalTimeMillis()
        );

        model.addAllAttributes(
                Map.ofEntries(
                        entry("page", pageResponse),
                        entry("genre", genre),
                        entry("startYear", startYear == null ? "" : startYear),
                        entry("term", term)
                )
        );
        return "search-titles-list";
    }

    @GetMapping(value = "/title/{id}")
    public String title(@PathVariable("id") long id, Model model) {
        StopWatch sw = new StopWatch();
        sw.start();

        ResponseEntity<TitleBasics> title = titleBasicsServiceClient.titleById(id);

        sw.stop();

        if (title.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new TitleNotFoundException();
        } else {
            LOGGER.info("Title #{} fetched in {}ms", id, sw.getTotalTimeMillis());
            model.addAttribute("title", title.getBody());
            return "title";
        }
    }

    @PostMapping("/startCrawling")
    public String startCrawling() {
        crawlerServiceClient.start();
        return "redirect:/sys-info";
    }

    @PostMapping("/stopCrawling")
    public String stopCrawling() {
        crawlerServiceClient.stop();
        return "redirect:/sys-info";
    }

    @PostMapping("/deleteTitleBasics")
    public String deleteTitleBasics() {
        titleBasicsServiceClient.deleteAll();
        return "redirect:/sys-info";
    }

    @GetMapping("/sys-info")
    public String systemInformation(Model model) {
        model.addAttribute("stats",
                Map.ofEntries(
                        entry("titleBasics", Objects.requireNonNull(titleBasicsServiceClient.stats().getBody())),
                        entry("crawler", Objects.requireNonNull(crawlerServiceClient.stats().getBody()))
                )
        );

        Map<String, Integer> discoveredServices = discoveryClient.getServices().stream()
                .collect(Collectors
                        .toMap(sn -> sn, sn -> discoveryClient.getInstances(sn).size())
                );

        model.addAttribute("services", discoveredServices);

        return "sys-info";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = TitleNotFoundException.class)
    public String titleNotFoundExceptionHandler(TitleNotFoundException e, Model model) {
        model.addAttribute("errorMessage", "Page Not Found");
        return "error";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        model.addAttribute("errorMessage", "Oops! Something goes wrong. Try again later");
        return "error";
    }
}
