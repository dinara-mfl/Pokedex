package org.example.pokedex.controller;

import jakarta.validation.Valid;
import org.example.pokedex.dto.FavoriteDto;
import org.example.pokedex.dto.UpdateFavoriteDto;
import org.example.pokedex.model.Favorite;
import org.example.pokedex.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<Favorite> getAllFavorites() {
        return service.getAllFavorites();
    }

    @GetMapping("/{id}")
    public Favorite getFavoriteById(@PathVariable("id") String id) {
        return service.getFavoriteById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable("id") String id) {
        service.deleteFavorite(id);
    }

    @PutMapping("/{id}")
    public Favorite updateFavorite(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateFavoriteDto request
    ) {
        return service.updateFavorite(id, request);
    }
}