package org.mderuijter.rockstarspringbootexercise;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mderuijter.rockstarspringbootexercise.artist.Artist;
import org.mderuijter.rockstarspringbootexercise.artist.ArtistRepository;
import org.mderuijter.rockstarspringbootexercise.song.Song;
import org.mderuijter.rockstarspringbootexercise.song.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class RockstarConfig {

    @Bean
    CommandLineRunner commandLineRunner(ArtistRepository artistRepository, SongRepository songRepository) {
        return args -> {
            ObjectMapper objectMapper = new ObjectMapper();

            TypeReference<List<Artist>> artistTypeReference = new TypeReference<>() {};
            TypeReference<List<Song>> songTypeReference = new TypeReference<>() {};

            InputStream inputStreamArtist = TypeReference.class.getResourceAsStream("/json/artists.json");
            InputStream inputStreamSong = TypeReference.class.getResourceAsStream("/json/songs.json");

            List<Song> songs = new ArrayList<>();
            List<Artist> artists = new ArrayList<>();

            try {
                songs = objectMapper.readValue(inputStreamSong, songTypeReference)
                        .stream()
                        .filter(
                                song -> song.getYear().compareTo(2016) < 0 && song.getGenre().contains("Metal"))
                        .collect(Collectors.toList());

                List<Song> finalSongs = songs;
                artists = objectMapper.readValue(inputStreamArtist, artistTypeReference)
                        .stream()
                        .filter(artist -> finalSongs.stream()
                                .anyMatch(song -> {
                                    Optional<String> songArtist = Optional.ofNullable(song.getArtist());
                                    Optional<String> artistName = Optional.ofNullable(artist.getName());
                                    return songArtist.equals(artistName);
                                }))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                System.out.println("Unable to save Artists or Songs: " + e.getMessage());
            }
            songRepository.saveAll(songs);
            artistRepository.saveAll(artists);
        };
    }
}
