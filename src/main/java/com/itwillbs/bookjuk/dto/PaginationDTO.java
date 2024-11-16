package com.itwillbs.bookjuk.dto;

import java.util.List;

public interface PaginationDTO<T> {
    List<T> getItems();
    int getTotalPages();
    int getCurrentPage();
    boolean hasNext();
    boolean hasPrevious();
}
