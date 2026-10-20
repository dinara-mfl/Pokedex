package org.example.pokedex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFavoriteDto(
        @NotBlank(message = "Nickname must not be blank")
        @Size(min = 2, message = "Nickname must contain at least 2 characters")
        String nickname
) {
}