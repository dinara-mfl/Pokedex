package org.example.pokedex.controller;

import jakarta.validation.constraints.NotBlank;
import org.example.pokedex.dto.PokemonDto;
import org.example.pokedex.service.PokemonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService service;

    public PokemonController(PokemonService service) {
        this.service = service;
    }

    @GetMapping("/{name}")
    public PokemonDto getPokemon(
            @PathVariable("name")
            @NotBlank(message = "Name darf nicht leer sein")
            String name
    ) {
        return service.getPokemon(name);
    }
}