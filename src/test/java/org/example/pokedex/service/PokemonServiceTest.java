package org.example.pokedex.service;

import org.example.pokedex.dto.PokemonDto;
import org.example.pokedex.exception.PokemonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class PokemonServiceTest {

    private PokemonService service;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        service = new PokemonService(builder);
    }

    @Test
    void getPokemon() {
        server.expect(requestTo("https://pokeapi.co/api/v2/pokemon/pikachu"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": 25,
                          "name": "pikachu",
                          "height": 4,
                          "weight": 60,
                          "types": [{"type": {"name": "electric"}}],
                          "sprites": {
                            "other": {
                              "official-artwork": {
                                "front_default": "https://example.com/pikachu.png"
                              }
                            }
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        PokemonDto actual = service.getPokemon("pikachu");

        assertEquals(new PokemonDto(
                null, "25", null, "pikachu",
                "https://example.com/pikachu.png",
                4, 60, List.of("electric")
        ), actual);

        server.verify();
    }

    @Test
    void getPokemon_whenMissing() {
        server.expect(requestTo("https://pokeapi.co/api/v2/pokemon/missing"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(
                PokemonNotFoundException.class,
                () -> service.getPokemon("missing")
        );

        server.verify();
    }
}