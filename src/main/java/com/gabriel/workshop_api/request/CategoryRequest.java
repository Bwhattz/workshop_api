package com.gabriel.workshop_api.request;

import com.gabriel.workshop_api.model.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(

        @NotBlank(message = "O nome da categoria precisa ser obrigátorio")
        String name
) {
    public Category toEntity() {

        return Category.builder()
                .name(this.name())
                .build();
    }
}
