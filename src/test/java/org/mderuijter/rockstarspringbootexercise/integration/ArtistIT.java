package org.mderuijter.rockstarspringbootexercise.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mderuijter.rockstarspringbootexercise.artist.Artist;
import org.mderuijter.rockstarspringbootexercise.artist.ArtistRepository;
import org.mderuijter.rockstarspringbootexercise.exception.ArtistNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class ArtistIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtistRepository artistRepository;

    private final Faker faker = new Faker();

    @Test
    void canAddNewArtist() throws Exception {
        // given
        String name = String.format(
                "%s",
                faker.artist().name()
        );

        Artist artist = new Artist(name);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/artists")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist)));

        // then
        resultActions.andExpect(status().isOk());
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(artist);
    }

    @Test
    void canUpdateArtist() throws Exception {
        // given
        String name = String.format(
                "%s",
                faker.artist().name()
        );
        String updatedName = String.format(
                "%s",
                faker.artist().name()
        );
        Artist artist = new Artist(name);

        mockMvc.perform(post("/api/v1/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isOk());

        MvcResult getArtistResult = mockMvc.perform(get("/api/v1/artists")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getArtistResult.getResponse().getContentAsString();

        List<Artist> artists = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        long id = artists.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .map(Artist::getId)
                .findFirst()
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found with name: " + name));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/artists/" + id)
                .param("name", updatedName));

        // then
        resultActions.andExpect(status().isOk());
        Optional<Artist> expected = artistRepository.findByNameIgnoreCase(updatedName);
        assertThat(expected.isPresent()).isTrue();
    }

    @Test
    void canDeleteArtist() throws Exception {
        // given
        String name = String.format(
                "%s",
                faker.artist().name()
        );
        Artist artist = new Artist(name);

        mockMvc.perform(post("/api/v1/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isOk());

        MvcResult getArtistResult = mockMvc.perform(get("/api/v1/artists/search")
                .param("name", name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getArtistResult.getResponse().getContentAsString();

        Artist artistResult = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        long id = artistResult.getId();

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/artists/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = artistRepository.existsById(id);
        assertThat(exists).isFalse();

    }
}
