package org.example.pokedex.exception;

public class CollectionEntryNotFoundException extends RuntimeException {

    public CollectionEntryNotFoundException(String id) {
        super("Collection entry not found: " + id);
    }
}