package org.mderuijter.rockstarspringbootexercise.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    public void addNewArtist(Artist artist){
        Optional<Artist> artistOptional = artistRepository.findByNameContainingIgnoreCase(artist.getName());
        if (artistOptional.isPresent()) {
            throw new IllegalStateException("Artist already exists");
        }
        artistRepository.save(artist);
    }

    public Artist getArtistByName(String name) {
        Optional<Artist> artistOptional = artistRepository.findByNameContainingIgnoreCase(name);
        if (artistOptional.isEmpty()) {
            throw new IllegalStateException("Artist not found with name: " + name);
        }
        return artistOptional.get();
    }

    @Transactional
    public void updateArtist(Long artistId, String name) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalStateException("Artist with id " + artistId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(artist.getName(), name)) {
            artist.setName(name);
        }
    }

    public void deleteArtist(Long artistId){
        boolean exists = artistRepository.existsById(artistId);
        if (!exists) {
            throw new IllegalStateException("Artist with id " + artistId + " does not exist");
        }
        artistRepository.deleteById(artistId);
    }
}
