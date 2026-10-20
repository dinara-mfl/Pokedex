package org.example.pokedex.controller;

import jakarta.validation.Valid;
import org.example.pokedex.dto.FavoriteDto;
import org.example.pokedex.model.Favorite;
import org.example.pokedex.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collection")
public class FavoriteController {

    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Favorite createFavorite(
            @Valid @RequestBody FavoriteDto request
    ) {
        return service.createFavorite(request);
    }
}