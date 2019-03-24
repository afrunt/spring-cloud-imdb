package com.afrunt.scimdb.dto.titlebasics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Frunt
 */
public class SearchRequest implements Serializable {
    private String term;
    private int page = 0;
    private int perPage = 20;
    private Integer startYear;
    private List<String> genres = new ArrayList<>();

    public String getTerm() {
        return term;
    }

    public SearchRequest setTerm(String term) {
        this.term = term;
        return this;
    }

    public int getPage() {
        return page;
    }

    public SearchRequest setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPerPage() {
        return perPage;
    }

    public SearchRequest setPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public SearchRequest setGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    public boolean hasGenres() {
        return genres != null
                && !genres.isEmpty()
                && genres.stream()
                .anyMatch(g -> g != null && !g.trim().isEmpty());
    }

    public Integer getStartYear() {
        return startYear;
    }

    public SearchRequest setStartYear(Integer startYear) {
        this.startYear = startYear;
        return this;
    }
}
