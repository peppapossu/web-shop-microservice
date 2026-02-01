package com.ks.notificationservice.mapper;

import com.ks.notificationservice.dto.PagedResponse;
import org.springframework.data.domain.Page;

public interface PagedResponseMapper {

    static <T> PagedResponse<T> map(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
