package org.mderuijter.rockstarspringbootexercise.artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {

    @Query("SELECT a FROM Artist a WHERE a.name LIKE %:name%")
    Optional<Artist> findByNameContainingIgnoreCase(String name);
}
