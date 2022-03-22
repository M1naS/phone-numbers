package com.jumia.phonenumbers.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ControllerUtils {
    public static Pageable createPageable(Integer page, Integer size, String sortBy, Sort.Direction direction) {
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public static Pageable createSortOnlyPageable(String sortBy, Sort.Direction direction) {
        return PageRequest.of(
                0,
                Integer.MAX_VALUE,
                Sort.by(direction, sortBy)
        );
    }
}
