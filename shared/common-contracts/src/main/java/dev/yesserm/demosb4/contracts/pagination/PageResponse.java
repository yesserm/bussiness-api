package dev.yesserm.demosb4.contracts.pagination;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    public PageResponse {
        content = content == null ? List.of() : List.copyOf(content);
    }
}
