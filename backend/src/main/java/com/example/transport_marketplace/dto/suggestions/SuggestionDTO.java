package com.example.transport_marketplace.dto.suggestions;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDTO {
    private List<?> data;
    private int count;
    private String query;
}
