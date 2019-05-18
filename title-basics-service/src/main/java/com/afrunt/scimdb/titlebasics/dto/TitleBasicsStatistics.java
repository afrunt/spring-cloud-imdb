package com.afrunt.scimdb.titlebasics.dto;

import com.afrunt.scimdb.dto.Dto;

import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Andrii Frunt
 */
public class TitleBasicsStatistics extends Dto {
    public long titles;
    public long genres;
    private boolean available = true;

    public boolean isAvailable() {
        return available;
    }

    public TitleBasicsStatistics setAvailable(boolean available) {
        this.available = available;
        return this;
    }

    public long getTitles() {
        return titles;
    }

    public TitleBasicsStatistics setTitles(long titles) {
        this.titles = titles;
        return this;
    }

    public long getGenres() {
        return genres;
    }

    public TitleBasicsStatistics setGenres(long genres) {
        this.genres = genres;
        return this;
    }

    public Map<String, Object> asMap() {
        return Map.ofEntries(
                entry("available", available),
                entry("titles", titles),
                entry("genres", genres)
        );
    }
}
