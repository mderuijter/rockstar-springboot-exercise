package org.mderuijter.rockstarspringbootexercise.song;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "shortName", "artist" }) })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {
    @Id
    @SequenceGenerator(
            name = "artist_sequence",
            sequenceName = "artist_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "artist_sequence"
    )
    private Long id;
    private String name;
    private Integer year;
    private String shortName;
    private Integer bpm;
    private Integer duration;
    private String genre;
    private String spotifyId;
    private String album;
    private String artist;

    public Song(){}

    @JsonCreator
    public Song(@JsonProperty("Name") String name,
                @JsonProperty("Year") Integer year,
                @JsonProperty("Shortname") String shortName,
                @JsonProperty("Bpm")Integer bpm,
                @JsonProperty("Duration") Integer duration,
                @JsonProperty("Genre") String genre,
                @JsonProperty("SpotifyId") String spotifyId,
                @JsonProperty("Artist") String album,
                @JsonProperty("Album") String artist) {
        this.name = name;
        this.year = year;
        this.shortName = shortName;
        this.bpm = bpm;
        this.duration = duration;
        this.genre = genre;
        this.spotifyId = spotifyId;
        this.album = album;
        this.artist = artist;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", shortName='" + shortName + '\'' +
                ", bpm=" + bpm +
                ", duration=" + duration +
                ", genre='" + genre + '\'' +
                ", spotifyId='" + spotifyId + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
