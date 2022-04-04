package org.mderuijter.rockstarspringbootexercise.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/artist")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<Artist> getArtists() {
        return artistService.getArtists();
    }

    @GetMapping(path = "search")
    public Artist getArtistByName(@RequestParam String name) {
        return artistService.getArtistByName(name);
    }

    @PostMapping
    public void createNewArtist(@RequestBody Artist artist) {
        artistService.addNewArtist(artist);
    }

    @PutMapping(path = "{artistId}")
    public void updateArtist(
            @PathVariable("artistId") Long artistId,
            @RequestParam(required = false) String name) {
        artistService.updateArtist(artistId, name);
    }

    @DeleteMapping(path = "{artistId}")
    public void deleteArtist(@PathVariable("artistId") Long artistId) {
        artistService.deleteArtist(artistId);
    }

}
