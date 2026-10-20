package org.example.pokedex.service;

import org.example.pokedex.dto.FavoriteDto;
import org.example.pokedex.dto.PokemonDto;
import org.example.pokedex.dto.UpdateFavoriteDto;
import org.example.pokedex.exception.CollectionEntryNotFoundException;
import org.example.pokedex.model.Favorite;
import org.example.pokedex.repository.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceTest {

    private final FavoriteRepository repository =
            Mockito.mock(FavoriteRepository.class);

    private final PokemonService pokemonService =
            Mockito.mock(PokemonService.class);

    private final FavoriteService service =
            new FavoriteService(pokemonService, repository);

    private final Favorite favorite = new Favorite(
            "favorite-id", "25", "Starter", "pikachu",
            "https://example.com/pikachu.png",
            4, 60, List.of("electric")
    );

    @Test
    void createFavorite() {
        PokemonDto pokemon = new PokemonDto(
                null, "25", null, "pikachu",
                favorite.pictureUrl(), 4, 60, List.of("electric")
        );

        Mockito.when(pokemonService.getPokemon("pikachu"))
                .thenReturn(pokemon);
        Mockito.when(repository.save(Mockito.any(Favorite.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Favorite actual = service.createFavorite(
                new FavoriteDto("pikachu", "Starter")
        );

        ArgumentCaptor<Favorite> captor =
                ArgumentCaptor.forClass(Favorite.class);
        Mockito.verify(repository).save(captor.capture());

        Favorite saved = captor.getValue();

        assertDoesNotThrow(() -> UUID.fromString(saved.id()));
        assertEquals(new Favorite(
                saved.id(), "25", "Starter", "pikachu",
                favorite.pictureUrl(), 4, 60, List.of("electric")
        ), saved);
        assertEquals(saved, actual);
    }

    @Test
    void getAllFavorites() {
        Mockito.when(repository.findAll()).thenReturn(List.of(favorite));

        assertEquals(List.of(favorite), service.getAllFavorites());
        Mockito.verifyNoInteractions(pokemonService);
    }

    @Test
    void getFavoriteById() {
        Mockito.when(repository.findById("favorite-id"))
                .thenReturn(Optional.of(favorite));

        assertEquals(favorite, service.getFavoriteById("favorite-id"));
        Mockito.verifyNoInteractions(pokemonService);
    }

    @Test
    void updateFavorite_changesOnlyNickname() {
        Favorite expected = new Favorite(
                favorite.id(), favorite.pokemonId(), "Champion",
                favorite.pokemonName(), favorite.pictureUrl(),
                favorite.height(), favorite.weight(), favorite.types()
        );

        Mockito.when(repository.findById("favorite-id"))
                .thenReturn(Optional.of(favorite));
        Mockito.when(repository.save(expected)).thenReturn(expected);

        Favorite actual = service.updateFavorite(
                "favorite-id", new UpdateFavoriteDto("Champion")
        );

        assertEquals(expected, actual);
        Mockito.verify(repository).save(expected);
        Mockito.verifyNoInteractions(pokemonService);
    }

    @Test
    void deleteFavorite() {
        Mockito.when(repository.findById("favorite-id"))
                .thenReturn(Optional.of(favorite));

        service.deleteFavorite("favorite-id");

        Mockito.verify(repository).delete(favorite);
        Mockito.verifyNoInteractions(pokemonService);
    }

    @Test
    void unknownId_throwsExceptionForReadUpdateAndDelete() {
        Mockito.when(repository.findById("missing"))
                .thenReturn(Optional.empty());

        assertThrows(CollectionEntryNotFoundException.class,
                () -> service.getFavoriteById("missing"));

        assertThrows(CollectionEntryNotFoundException.class,
                () -> service.updateFavorite(
                        "missing", new UpdateFavoriteDto("Champion")
                ));

        assertThrows(CollectionEntryNotFoundException.class,
                () -> service.deleteFavorite("missing"));

        Mockito.verify(repository, Mockito.never())
                .save(Mockito.any(Favorite.class));
        Mockito.verify(repository, Mockito.never())
                .delete(Mockito.any(Favorite.class));
        Mockito.verifyNoInteractions(pokemonService);
    }
}