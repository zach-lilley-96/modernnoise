package com.lilley.modernnoise.Repos;

import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepo extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByUserAndAlbum(User user, Album album);
    boolean existsByUserAndAlbum(User user, Album album);

    List<Rating> findByUser(User user);

    @Query("SELECT r FROM Rating r WHERE r.user = ?1 AND r.album.artist.id = ?2")
    List<Rating> findByUserAndArtist(User user, UUID artistId);
}
