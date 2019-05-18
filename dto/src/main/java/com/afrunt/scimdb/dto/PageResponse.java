package com.afrunt.scimdb.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Frunt
 */
public class PageResponse<T> {
    private int page;
    private int pages;
    private int perPage;
    private long total;

    private List<T> items = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public PageResponse<T> setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public PageResponse<T> setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public int getPerPage() {
        return perPage;
    }

    public PageResponse<T> setPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageResponse<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public PageResponse<T> setItems(List<T> items) {
        this.items = items;
        return this;
    }

    public int getItemsCount(){
        return getItems() == null ? 0 : getItems().size();
    }
}
