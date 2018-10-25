package com.xhinliang.birthdays.common.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PageItem<T> {

    private int currentPage;
    private boolean hasNextPage;
    private List<T> items;

    @JsonCreator
    public PageItem() {
    }

    private PageItem(int currentPage, boolean hasNextPage, List<T> items) {
        this.currentPage = currentPage;
        this.hasNextPage = hasNextPage;
        this.items = items;
    }

    public static <T> PageItem<T> of(int currentPage, boolean hasNextPage, List<T> items) {
        return new PageItem<>(currentPage, hasNextPage, items);
    }

    public static <T> PageItem<T> of(Page<T> page) {
        return new PageItem<>(page.getNumber(), page.hasNext(), page.getContent());
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public List<T> getItems() {
        return items;
    }

    public <R> PageItem<R> map(Function<? super T, ? extends R> mapper) {
        List<R> list = this.getItems().stream() //
            .map(mapper)
            .collect(toList());
        return PageItem.of(this.currentPage, this.hasNextPage, list);
    }
}
