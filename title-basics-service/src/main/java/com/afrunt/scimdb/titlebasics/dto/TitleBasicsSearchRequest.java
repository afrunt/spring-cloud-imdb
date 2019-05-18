package com.afrunt.scimdb.titlebasics.dto;

import com.afrunt.scimdb.dto.PageRequestDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Frunt
 */
public class TitleBasicsSearchRequest extends PageRequestDto {
    private String keywords;
    private Integer startYear;
    private List<String> genres = new ArrayList<>();

    public String getKeywords() {
        return keywords;
    }

    public TitleBasicsSearchRequest setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public TitleBasicsSearchRequest setGenres(List<String> genres) {
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

    public TitleBasicsSearchRequest setStartYear(Integer startYear) {
        this.startYear = startYear;
        return this;
    }
}
