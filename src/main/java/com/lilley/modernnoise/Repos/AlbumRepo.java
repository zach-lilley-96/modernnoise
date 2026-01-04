package com.lilley.modernnoise.Repos;

import com.lilley.modernnoise.Data.Entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlbumRepo extends JpaRepository<Album, UUID> {
    boolean existsByAudioDbId(String audioDbId);
    List<Album> findByArtistIdOrderByReleaseYear(UUID artistId);
}
