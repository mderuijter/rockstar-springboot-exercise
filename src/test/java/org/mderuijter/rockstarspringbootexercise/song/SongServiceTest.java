package org.mderuijter.rockstarspringbootexercise.song;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock private SongRepository songRepository;
    private SongService underTest;

    @BeforeEach
    void SetUp() {
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
                .isInstanceOf(IllegalStateException.class)
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
                .isInstanceOf(IllegalStateException.class)
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
    void willThrowWhen

    @Ignore
    @Test
    void updateSong() {
    }

    @Ignore
    @Test
    void deleteSong() {
    }
}