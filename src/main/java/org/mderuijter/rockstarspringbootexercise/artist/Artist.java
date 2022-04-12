package org.mderuijter.rockstarspringbootexercise.artist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
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

    @Column(unique = true)
    private String name;

    public Artist(){}

    public Artist(@JsonProperty("Name") String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
