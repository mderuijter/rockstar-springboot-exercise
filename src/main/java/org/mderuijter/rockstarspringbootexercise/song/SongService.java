package org.mderuijter.rockstarspringbootexercise.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    public void addNewSong(Song song) {
        if (!song.getGenre().contains("Metal") && song.getYear() < 2016) {
            throw new IllegalStateException("Song year must be before 2016 and be a Metal genre");
        }
        songRepository.save(song);
    }

    public List<Song> getSongsByGenre(String genre) {
        List<Song> songsByGenre = songRepository.findSongsByGenre(genre);
        if (songsByGenre.isEmpty()) {
            throw new IllegalStateException("No songs found for genre: " + genre);
        }
        return songsByGenre;
    }

    @Transactional
    public void updateSong(Long songId, String name, Integer year,
                           String shortName, Integer bpm, Integer duration, String genre,
                           String spotifyId, String album, String artist) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalStateException("Song with id " + songId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(song.getName(), name)) {
            song.setName(name);
        }

        if (year != 0 && !Objects.equals(song.getYear(), year) && year < 2016) {
            song.setYear(year);
        }

        if (shortName != null && shortName.length() > 0 && !Objects.equals(song.getShortName(), shortName)) {
            song.setShortName(shortName);
        }

        if (bpm != 0 && !Objects.equals(song.getBpm(), bpm)) {
            song.setBpm(bpm);
        }

        if (duration != 0 && !Objects.equals(song.getDuration(), duration)) {
            song.setDuration(duration);
        }

        if (genre != null && genre.length() > 0 && !Objects.equals(song.getGenre(), genre) && genre.contains("Metal")) {
            song.setGenre(genre);
        }

        if (spotifyId != null && spotifyId.length() > 0 && !Objects.equals(song.getSpotifyId(), spotifyId)) {
            song.setSpotifyId(spotifyId);
        }

        if (album != null && album.length() > 0 && !Objects.equals(song.getAlbum(), album)) {
            song.setAlbum(album);
        }

        if (artist != null && artist.length() > 0 && !Objects.equals(song.getArtist(), artist)) {
            song.setArtist(artist);
        }
    }

    public void deleteSong(Long songId) {
        boolean exists = songRepository.existsById(songId);
        if (!exists) {
            throw new IllegalStateException("Song with id " + songId + " does not exist");
        }
        songRepository.deleteById(songId);
    }
}
