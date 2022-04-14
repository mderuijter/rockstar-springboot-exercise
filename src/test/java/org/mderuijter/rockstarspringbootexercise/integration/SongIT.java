package org.mderuijter.rockstarspringbootexercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mderuijter.rockstarspringbootexercise.exception.SongNotFoundException;
import org.mderuijter.rockstarspringbootexercise.song.Song;
import org.mderuijter.rockstarspringbootexercise.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class SongIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SongRepository songRepository;

    private final Faker faker = new Faker();

    @Test
    void canAddNewSong() throws Exception {
        // given
        String name = String.format(
                "%s",
                faker.animal().name()
        );
        Integer year = Integer.valueOf(String.format(
                "%s",
                faker.number().numberBetween(1900, 2015)
        ));
        String shortName = String.format(
                "%s",
                faker.name().username()
        );
        Integer bpm = Integer.valueOf(String.format(
                "%s",
                faker.number().digits(3)
        ));
        Integer duration = Integer.valueOf(String.format(
                "%s",
                faker.number().digits(6)
        ));

        String genre = "Metal";

        String spotifyId = String.format(
                "%s",
                faker.idNumber().valid()
        );
        String album = String.format(
                "%s",
                faker.chuckNorris().fact()
        );
        String artist = String.format(
                "%s",
                faker.artist().name()
        );
        Song song = new Song(name, year, shortName, bpm, duration, genre, spotifyId, album, artist);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(song)));

        // then
        resultActions.andExpect(status().isOk());
        List<Song> songs = songRepository.findAll();
        assertThat(songs).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(song);
    }

    @Test
    void canUpdateSong() throws Exception {
        // given
        Song song = new Song(
                "(Funky) Sex Farm", 2009, "funkysexfarm", 107, 260068, "Metal",
                "1VfdMD8JguRISrccgLifIZ","Back From The Dead", "Spinal Tap"
        );
        String updatedName = String.format(
                "%s",
                faker.animal().name()
        );
        Integer updatedYear = Integer.valueOf(String.format(
                "%s",
                faker.number().numberBetween(1900, 2015)
        ));
        String updatedShortName = String.format(
                "%s",
                faker.name().username()
        );
        Integer updatedBpm = Integer.valueOf(String.format(
                "%s",
                faker.number().digits(3)
        ));
        Integer updatedDuration = Integer.valueOf(String.format(
                "%s",
                faker.number().digits(6)
        ));

        String updatedGenre = "Metal";

        String updatedSpotifyId = String.format(
                "%s",
                faker.idNumber().valid()
        );
        String updatedAlbum = String.format(
                "%s",
                faker.chuckNorris().fact()
        );
        String updatedArtist = String.format(
                "%s",
                faker.artist().name()
        );

        mockMvc.perform(post("/api/v1/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(song)))
                .andExpect(status().isOk());

        MvcResult getSongResult = mockMvc.perform(get("/api/v1/songs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getSongResult.getResponse().getContentAsString();

        List<Song> songs = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        long id = songs.stream()
                .filter(s -> s.getSpotifyId().equalsIgnoreCase(song.getSpotifyId()))
                .map(Song::getId)
                .findFirst()
                .orElseThrow(() -> new SongNotFoundException("Song with spotifyId " + song.getSpotifyId() + " does not exist"));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/songs/" + id)
                .param("name", updatedName)
                .param("year", String.valueOf(updatedYear))
                .param("shortName", updatedShortName)
                .param("bpm", String.valueOf(updatedBpm))
                .param("duration", String.valueOf(updatedDuration))
                .param("genre", updatedGenre)
                .param("spotifyId", updatedSpotifyId)
                .param("album", updatedAlbum)
                .param("artist", updatedArtist));

        // then
        resultActions.andExpect(status().isOk());
        Optional<Song> optionalSong = songRepository.findById(id);
        Song expected = optionalSong.orElse(null);
        assertThat(expected).extracting(Song::getName, Song::getYear, Song::getShortName, Song::getBpm, Song::getDuration,
                Song::getGenre, Song::getSpotifyId, Song::getAlbum, Song::getArtist).containsOnly(updatedName, updatedYear,
                updatedShortName, updatedBpm, updatedDuration, updatedGenre, updatedSpotifyId, updatedAlbum, updatedArtist);
    }

    @Test
    void canDeleteSong() throws Exception {
        // given
        String name = String.format(
                "%s",
                faker.animal().name()
        );
        Integer year = Integer.valueOf(String.format(
                "%s",
                faker.number().numberBetween(1900, 2015)
        ));
        String shortName = String.format(
                "%s",
                faker.name().username()
        );
        Integer bpm = Integer.valueOf(String.format(
                "%s",
                faker.number().digits(3)
        ));
        Integer duration = Integer.valueOf(String.format(
                "%s",
                faker.number().digits(6)
        ));

        String genre = "Metal";

        String spotifyId = String.format(
                "%s",
                faker.idNumber().valid()
        );
        String album = String.format(
                "%s",
                faker.chuckNorris().fact()
        );
        String artist = String.format(
                "%s",
                faker.artist().name()
        );
        Song song = new Song(name, year, shortName, bpm, duration, genre, spotifyId, album, artist);

        mockMvc.perform(post("/api/v1/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song)))
                .andExpect(status().isOk());

        MvcResult getSongResult = mockMvc.perform(get("/api/v1/songs/search")
                .param("genre", genre)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getSongResult.getResponse().getContentAsString();
        List<Song> songs = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        long id = songs.stream()
                .peek(System.out::println)
                .filter(song1 -> Objects.nonNull(song1.getSpotifyId()))
                .filter(s -> s.getSpotifyId().equalsIgnoreCase(spotifyId))
                .map(Song::getId)
                .findFirst()
                .orElseThrow(() -> new SongNotFoundException("Song with spotifyId " + song.getSpotifyId() + " does not exist"));

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/songs/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = songRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
