package org.mderuijter.rockstarspringbootexercise.artist;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindArtistByNameIgnoringCase() {
        // given
        String name = "system of a down";
        Artist artist = new Artist("System of a Down");
        underTest.save(artist);

        // when
        Optional<Artist> expected = underTest.findByNameIgnoreCase(name);

        // then
        assertThat(expected.isPresent()).isTrue();
    }

    @Test
    void itShouldFindNoArtistByName() {
        // given
        String name = "Weird";
        Artist artist = new Artist("System of a Down");
        underTest.save(artist);

        // when
        Optional<Artist> expected = underTest.findByNameIgnoreCase(name);

        // then
        assertThat(expected.isEmpty()).isTrue();
    }
}