package org.example.pokedex.exception;

public class FavoriteNotFoundException extends RuntimeException {

    public FavoriteNotFoundException(String id) {
        super("Favorite with ID " + id + " was not found");
    }
}