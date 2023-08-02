package ru.practicum.main.state;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pagination extends PageRequest {


    public static final Sort SORT_EVENT_DATE = Sort.by("eventDate").ascending();
    public static final Sort SORT_VIEWS = Sort.by("views").ascending();

    protected Pagination(int from, int size, Sort sort) {
        super(from, size, sort);
    }

    public static Pageable patternPageable(Integer from, Integer size) {
        return of(from / size, size);
    }

    public static Pageable patternPageableWithSort(Integer from, Integer size, EventSortType sort) {
        if (sort != null) {
            if (sort.equals(EventSortType.EVENT_DATE)) {
                return of(from / size, size, SORT_EVENT_DATE);
            } else {
                return of(from / size, size, SORT_VIEWS);
            }
        } else {
            return of(from / size, size);
        }
    }
}
