package com.gabriel.workshop_api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabriel.workshop_api.model.Category;

import java.util.List;

public record CategoryResponse(
        String name,

        @JsonIgnore
        List<EventResponse> eventResponse
) {
    public static CategoryResponse fromCategory(Category category) {

        return new CategoryResponse(
                category.getName(),
                category.getEvents() != null ? category.getEvents().stream()
                                               .map(EventResponse::fromEvent)
                                               .toList() : List.of()
        );
    }
}
