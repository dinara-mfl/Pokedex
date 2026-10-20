package org.example.pokedex.dto;

import java.util.List;

public record PokemonDto(
        String id,
        String pokemonId,
        String nickname,
        String pokemonName,
        String pictureUrl,
        int height,
        int weight,
        List<String> types
) { }