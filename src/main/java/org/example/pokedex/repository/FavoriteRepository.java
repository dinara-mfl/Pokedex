package org.example.pokedex.repository;

import org.example.pokedex.model.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavoriteRepository
        extends MongoRepository<Favorite, String> {
}