package org.mderuijter.rockstarspringbootexercise.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/songs")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public List<Song> getSongs() {
        return songService.getSongs();
    }

    @GetMapping(path = "search")
    public List<Song> getSongsByGenre(@RequestParam String genre) {
        return songService.getSongsByGenre(genre);
    }

    @PostMapping
    public void createNewSong(@RequestBody Song song) {
        songService.addNewSong(song);
    }

    @PutMapping(path = "{songId}")
    public void updateSong(
            @PathVariable("songId") Long songId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) Integer bpm,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String spotifyId,
            @RequestParam(required = false) String album,
            @RequestParam(required = false) String artist) {
        songService.updateSong(songId, name, year, shortName, bpm, duration, genre, spotifyId, album, artist);
    }

    @DeleteMapping(path = "{songId}")
    public void deleteSong(@PathVariable("songId") Long songId) {
        songService.deleteSong(songId);
    }

}
