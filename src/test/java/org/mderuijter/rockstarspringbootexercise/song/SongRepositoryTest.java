package org.mderuijter.rockstarspringbootexercise.song;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SongRepositoryTest {

    @Autowired
    SongRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindSongsByGenreIgnoringCase() {
        // given
        String genre = "metal";
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
        underTest.saveAll(List.of(song1,song2,song3));

        // when
        List<Song> expected = underTest.findSongsByGenreIgnoringCase(genre);

        // then
        assertThat(expected).extracting("genre").containsOnly("Metal").hasSize(2);
    }

    @Test
    void itShouldFindNoSongsByGenre() {
        // given
        String genre = "Hip Hop";
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
        underTest.saveAll(List.of(song1,song2,song3));

        // when
        List<Song> expected = underTest.findSongsByGenreIgnoringCase(genre);

        // then
        assertThat(expected).hasSize(0);


    }
}