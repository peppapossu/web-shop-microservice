package com.ks.inventoryservice.item.api.dto;

//import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PagedResponse<T>(
//        @Schema(description = "Data")
        List<T> content,

//        @Schema(description = "Current page", example = "0")
        int page,

//        @Schema(description = "Page size", example = "20")
        int size,

//        @Schema(description = "Total number of elements", example = "145")
        long totalElements,

//        @Schema(description = "Total pages", example = "8")
        int totalPages,

//        @Schema(description = "Last page", example = "true")
        boolean last
) {
}
