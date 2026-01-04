package com.lilley.modernnoise.Repos;

import com.lilley.modernnoise.Data.Entities.Artist;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepo extends JpaRepository<Artist, UUID> {
//    Artist findByName(@NotNull String name);
    Artist findByAudioDbId(@NotNull String audioDbId);
    boolean existsByAudioDbId(@NotNull String audioDbId);
    boolean existsByName(@NotNull String name);
    Optional<Artist> findByNameIgnoreCase(@NotNull String name);

}
