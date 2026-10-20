package org.example.pokedex.service;

import org.example.pokedex.dto.FavoriteDto;
import org.example.pokedex.dto.PokemonDto;
import org.example.pokedex.exception.FavoriteNotFoundException;
import org.example.pokedex.model.Favorite;
import org.example.pokedex.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {

    private final PokemonService pokemonService;
    private final FavoriteRepository repository;

    public FavoriteService(
            PokemonService pokemonService,
            FavoriteRepository repository
    ) {
        this.pokemonService = pokemonService;
        this.repository = repository;
    }

    public Favorite createFavorite(FavoriteDto request) {
        PokemonDto pokemon = pokemonService.getPokemon(request.pokemonName());

        Favorite favorite = new Favorite(
                UUID.randomUUID().toString(),
                pokemon.pokemonId(),
                request.nickname(),
                pokemon.pokemonName(),
                pokemon.pictureUrl(),
                pokemon.height(),
                pokemon.weight(),
                pokemon.types()
        );

        return repository.save(favorite);
    }

    public List<Favorite> getAllFavorites() {
        return repository.findAll();
    }

    public Favorite getFavoriteById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException(id));
    }

    public void deleteFavorite(String id) {
        Favorite favorite = getFavoriteById(id);
        repository.delete(favorite);
    }
}