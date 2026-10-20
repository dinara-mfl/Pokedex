package org.example.pokedex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiResponse(
        int id,
        String name,
        int height,
        int weight,
        List<TypeEntry> types,
        Sprites sprites
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TypeEntry(TypeInfo type) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TypeInfo(String name) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sprites(Other other) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Other(
            @JsonProperty("official-artwork")
            Artwork officialArtwork
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Artwork(
            @JsonProperty("front_default")
            String frontDefault
    ) {
    }
}