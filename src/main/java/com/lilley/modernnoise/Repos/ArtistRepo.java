package com.lilley.modernnoise.Repos;

import com.lilley.modernnoise.Data.Entities.Artist;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtistRepo extends JpaRepository<Artist, UUID> {
    Artist findByName(@NotNull String name);
    boolean existsByAudioDbId(@NotNull String audioDbId);
}
