package org.example.pokedex.service;

import org.example.pokedex.dto.PokeApiResponse;
import org.example.pokedex.dto.PokemonDto;
import org.example.pokedex.exception.PokemonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class PokemonService {

    private final RestClient restClient;

    public PokemonService(RestClient.Builder builder) {
        restClient = builder
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }

    public PokemonDto getPokemon(String name) {
        PokeApiResponse response;

        try {
            response = restClient.get()
                    .uri("/pokemon/{name}", name.trim().toLowerCase(Locale.ROOT))
                    .retrieve()
                    .body(PokeApiResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new PokemonNotFoundException(name);
        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "PokéAPI konnte nicht geladen werden",
                    exception
            );
        }

        if (response == null) {
            throw new PokemonNotFoundException(name);
        }

        String pictureUrl = null;

        if (response.sprites() != null
                && response.sprites().other() != null
                && response.sprites().other().officialArtwork() != null) {
            pictureUrl = response.sprites()
                    .other()
                    .officialArtwork()
                    .frontDefault();
        }

        return new PokemonDto(
                null,
                String.valueOf(response.id()),
                null,
                response.name(),
                pictureUrl,
                response.height(),
                response.weight(),
                response.types().stream()
                        .map(entry -> entry.type().name())
                        .toList()
        );
    }
}