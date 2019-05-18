package com.afrunt.scimdb.titlebasics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Frunt
 */
@Document(indexName = "title-basics", type = "TitleBasics")
public class TitleBasicsDocument implements Mappable {
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

    public TitleBasicsDocument setTitleId(Long titleId) {
        this.titleId = titleId;
        return this;
    }

    public String getTitleType() {
        return titleType;
    }

    public TitleBasicsDocument setTitleType(String titleType) {
        this.titleType = titleType;
        return this;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public TitleBasicsDocument setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public TitleBasicsDocument setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public boolean isAdult() {
        return adult;
    }

    public TitleBasicsDocument setAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public TitleBasicsDocument setStartYear(Integer startYear) {
        this.startYear = startYear;
        return this;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public TitleBasicsDocument setEndYear(Integer endYear) {
        this.endYear = endYear;
        return this;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public TitleBasicsDocument setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public TitleBasicsDocument setGenres(List<String> genres) {
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

