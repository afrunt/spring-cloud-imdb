package com.afrunt.scimdb.dto;

/**
 * @author Andrii Frunt
 */
public class PageRequestDto extends Dto {
    private int page = 0;
    private int perPage = 20;
    private String sortBy;
    private boolean descending;

    public int getPage() {
        return page;
    }

    public PageRequestDto setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPerPage() {
        return perPage;
    }

    public PageRequestDto setPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public PageRequestDto setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public boolean isDescending() {
        return descending;
    }

    public PageRequestDto setDescending(boolean descending) {
        this.descending = descending;
        return this;
    }
}
