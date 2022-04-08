package org.mderuijter.rockstarspringbootexercise.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {

    List<Song> findSongsByGenreIgnoringCase(@Param("genre") String genre);
}
