package org.mderuijter.rockstarspringbootexercise.artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {

    Optional<Artist> findByNameIgnoreCase(@Param("name") String name);
}
