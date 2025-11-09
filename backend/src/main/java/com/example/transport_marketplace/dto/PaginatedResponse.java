package com.example.transport_marketplace.dto;

import java.util.List;

public record PaginatedResponse<T> (
        List<T> content,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages
) {}
