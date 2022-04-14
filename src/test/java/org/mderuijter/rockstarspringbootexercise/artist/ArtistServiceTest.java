package org.mderuijter.rockstarspringbootexercise.artist;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mderuijter.rockstarspringbootexercise.exception.ArtistNotFoundException;
import org.mderuijter.rockstarspringbootexercise.exception.ConflictException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    private ArtistService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ArtistService(artistRepository);
    }

    @Test
    void canGetAllArtists() {
        // when
        underTest.getArtists();

        // then
        verify(artistRepository).findAll();
    }

    @Test
    void canAddNewArtist() {
        // given
        Artist artist = new Artist("System of a Down");

        // when
        underTest.addNewArtist(artist);

        // then
        ArgumentCaptor<Artist> artistArgumentCaptor = ArgumentCaptor.forClass(Artist.class);
        verify(artistRepository).save(artistArgumentCaptor.capture());

        Artist capturedArtist = artistArgumentCaptor.getValue();

        assertThat(capturedArtist).isEqualTo(artist);
    }

    @Test
    void willThrowWhenArtistAlreadyExists() {
        // given
        Artist artist = new Artist("System of a Down");
        given(artistRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.of(artist));

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewArtist(artist))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Artist with name: " + artist.getName() + " already exists");
    }
    @Test
    void canGetArtistByName() {
        // given
        String name = "system of a down";
        Artist artist = new Artist("System of a Down");
        when(artistRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Optional.of(artist));

        // when
        Artist expected = underTest.getArtistByName(name);

        // then
        assertThat(expected).hasFieldOrPropertyWithValue("name", artist.getName());
        verify(artistRepository, times(1)).findByNameIgnoreCase(name);
    }

    @Test
    void willThrowWhenArtistNotFoundByName() {
        // given
        String name = "system of a down";
        given(artistRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getArtistByName(name))
                .isInstanceOf(ArtistNotFoundException.class)
                .hasMessageContaining("Artist not found with name: " + name);
        verify(artistRepository, times(1)).findByNameIgnoreCase(name);
    }

    @Test
    void canUpdateArtist() {
        // given
        long id = 10;
        Artist originalArtist = new Artist("System of a Down");
        given(artistRepository.findById(anyLong()))
                .willReturn(Optional.of(originalArtist));

        // when
        underTest.updateArtist(id, "Linkin Park");

        // then
        assertThat(originalArtist.getName()).isEqualTo("Linkin Park");
    }

    @Test
    void canDeleteArtist() {
        // given
        long id = 10;
        given(artistRepository.existsById(id))
                .willReturn(true);

        // when
        underTest.deleteArtist(id);

        // then
        verify(artistRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteArtistNotFound() {
        // given
        long id = 10;
        given(artistRepository.existsById(id))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteArtist(id))
                .isInstanceOf(ArtistNotFoundException.class)
                .hasMessageContaining("Artist with id " + id + " does not exist");
    }
}