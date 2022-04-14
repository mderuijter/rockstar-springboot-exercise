package org.mderuijter.rockstarspringbootexercise.song;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mderuijter.rockstarspringbootexercise.exception.ConflictException;
import org.mderuijter.rockstarspringbootexercise.exception.SongNotFoundException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;
    private SongService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SongService(songRepository);
    }

    @Test
    void canGetAllSongs() {
        // when
        underTest.getSongs();

        // then
        verify(songRepository).findAll();
    }

    @Test
    void canAddNewSong() {
        // given
        Song song1 = new Song(
                "(Funky) Sex Farm", 2009, "funkysexfarm", 107, 260068, "Metal",
                "1VfdMD8JguRISrccgLifIZ","Back From The Dead", "Spinal Tap"
        );

        // when
        underTest.addNewSong(song1);

        // then
        ArgumentCaptor<Song> songArgumentCaptor = ArgumentCaptor.forClass(Song.class);
        verify(songRepository).save(songArgumentCaptor.capture());

        Song capturedSong = songArgumentCaptor.getValue();

        assertThat(capturedSong).isEqualTo(song1);
    }

    @Test
    void willThrowWhenSongGenreIsNotMetal() {
        // given
        Song song1 = new Song(
                "(Funky) Sex Farm", 2009, "funkysexfarm", 107, 260068, "Rock",
                "1VfdMD8JguRISrccgLifIZ","Back From The Dead", "Spinal Tap"
        );

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewSong(song1))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Song must be from before 2016 and be a Metal genre");
        verify(songRepository, never()).save(any());
    }

    @Test
    void willThrowWhenSongYearIsNotBefore2016() {
        // given
        Song song1 = new Song(
                "(Funky) Sex Farm", 2016, "funkysexfarm", 107, 260068, "Metal",
                "1VfdMD8JguRISrccgLifIZ","Back From The Dead", "Spinal Tap"
        );

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewSong(song1))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Song must be from before 2016 and be a Metal genre");
        verify(songRepository, never()).save(any());
    }

    @Test
    void canGetSongsByGenre() {
        // given
        String genre = "metal";
        List<Song> songList = new java.util.ArrayList<>(Collections.emptyList());
        Song song1 = new Song(
                "(Funky) Sex Farm", 2009, "funkysexfarm", 107, 260068, "Metal",
                "1VfdMD8JguRISrccgLifIZ","Back From The Dead", "Spinal Tap"
        );
        Song song2 = new Song(
                "(Don't Fear) The Reaper", 1975, "dontfearthereaper", 141, 322822,
                "Classic Rock" ,"5QTxFnGygVM4jFQiBovmRo", "Agents of Fortune", "Blue Öyster Cult"
        );
        Song song3 = new Song(
                "...And Justice for All", 1988, "andjusticeforall", 97, 591218, "Metal",
                "2VIk1zgNk6aRiIQ9C1T4Yu", "Metallica", "...And Justice for All"
        );
        songList.add(song1);
        songList.add(song2);
        songList.add(song3);

        when(songRepository.findSongsByGenreIgnoringCase(anyString()))
                .thenReturn(songList);

        // when
        // then
        List<Song> expectedList = underTest.getSongsByGenre(genre);

        assertThat(expectedList).asList().hasSize(3);
        verify(songRepository, times(1)).findSongsByGenreIgnoringCase(genre);
    }

    @Test
    void willThrowWhenSongsNotFoundForGenre() {
        // given
        String genre = "Metal";
        given(songRepository.findSongsByGenreIgnoringCase(anyString()))
                .willReturn(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> underTest.getSongsByGenre(genre))
                .isInstanceOf(SongNotFoundException.class)
                .hasMessageContaining("No songs found for genre: " + genre);
        verify(songRepository, times(1)).findSongsByGenreIgnoringCase(genre);
    }

    @Test
    void canUpdateSong() {
        // given
        long id = 10;
        Song originalSong = new Song(
                "(Funky) Sex Farm", 2009, "funkysexfarm", 107, 260068, "Classic Rock",
                "1VfdMD8JguRISrccgLifIZ","Back From The Dead", "Spinal Tap"
        );
        given(songRepository.findById(anyLong()))
                .willReturn(Optional.of(originalSong));

        // when
        underTest.updateSong(id, "(Don't Fear) The Reaper", 1975, "dontfearthereaper", 141, 322822,
                "Metal" ,"5QTxFnGygVM4jFQiBovmRo", "Agents of Fortune", "Blue Öyster Cult");


        // then
        assertThat(originalSong).extracting(Song::getName, Song::getYear, Song::getShortName, Song::getBpm, Song::getDuration,
                Song::getGenre, Song::getSpotifyId, Song::getAlbum, Song::getArtist).containsOnly("(Don't Fear) The Reaper",
                1975, "dontfearthereaper", 141, 322822, "Metal" ,"5QTxFnGygVM4jFQiBovmRo", "Agents of Fortune",
                "Blue Öyster Cult");
    }

    @Test
    void canDeleteSong() {
        // given
        long id = 10;
        given(songRepository.existsById(id))
                .willReturn(true);

        // when
        underTest.deleteSong(id);

        // then
        verify(songRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteSongNotFound() {
        // given
        long id = 10;
        given(songRepository.existsById(id))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteSong(id))
                .isInstanceOf(SongNotFoundException.class)
                .hasMessageContaining("Song with id " + id + " does not exist");
        verify(songRepository, never()).deleteById(any());
    }
}