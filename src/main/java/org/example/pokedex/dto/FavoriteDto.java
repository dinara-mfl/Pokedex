package org.example.pokedex.dto;

import jakarta.validation.constraints.NotBlank;

public record FavoriteDto(
        @NotBlank(message = "Pokemon name must not be blank")
        String pokemonName,

        String nickname
) {
}