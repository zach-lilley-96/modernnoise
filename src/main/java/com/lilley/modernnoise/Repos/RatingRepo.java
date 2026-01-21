package com.lilley.modernnoise.Repos;

import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepo extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByUserAndAlbum(User user, Album album);

    @Query("""
       SELECT r
       FROM Rating r
       WHERE r.user = :user
       AND r.album.audioDbId IN :albumIds
       """)
    Optional<List<Rating>> findByUserAndAlbumIds(User user, List<String> albumIds);
    boolean existsByUserAndAlbum(User user, Album album);

    List<Rating> findByUser(User user);

    @Query(value = """
                SELECT DISTINCT a.artist
                FROM Rating r
                JOIN r.album a
                WHERE r.user = :user
            """,
            countQuery = """
                SELECT COUNT(DISTINCT a.artist)
                FROM Rating r
                JOIN r.album a
                WHERE r.user = :user
            """)
    Page<Artist> findDistinctArtistsRatedByUser(User user, Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.user = ?1 AND r.album.artist.audioDbId = ?2")
    List<Rating> findByUserAndArtistAudioDbId(User user, String audioDbId);
}
