package com.afrunt.scimdb.titlebasics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Frunt
 */
@Document(indexName = "title-basics", type = "TitleBasics")
public class TitleBasicsES implements Mappable {
    @Id
    private Long titleId;

    private String titleType;

    private String primaryTitle;

    private String originalTitle;

    private boolean adult;

    private Integer startYear;
    private Integer endYear;
    private Integer runtimeMinutes;

    private List<String> genres = new ArrayList<>();

    public Long getTitleId() {
        return titleId;
    }

    public TitleBasicsES setTitleId(Long titleId) {
        this.titleId = titleId;
        return this;
    }

    public String getTitleType() {
        return titleType;
    }

    public TitleBasicsES setTitleType(String titleType) {
        this.titleType = titleType;
        return this;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public TitleBasicsES setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public TitleBasicsES setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public boolean isAdult() {
        return adult;
    }

    public TitleBasicsES setAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public TitleBasicsES setStartYear(Integer startYear) {
        this.startYear = startYear;
        return this;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public TitleBasicsES setEndYear(Integer endYear) {
        this.endYear = endYear;
        return this;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public TitleBasicsES setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public TitleBasicsES setGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    @Override
    public String toString() {
        return "TitleBasics{" +
                "titleId=" + titleId +
                ", titleType='" + titleType + '\'' +
                ", primaryTitle='" + primaryTitle + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", adult=" + adult +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", runtimeMinutes=" + runtimeMinutes +
                ", genres=" + genres +
                '}';
    }
}

