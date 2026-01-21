package com.lilley.modernnoise.Services.Interfaces;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Dtos.Response.RatingResponseDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Mappers.RatingMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRatingService {
    Optional<RatingDto> rateAlbum(User user, String albumMusicBrainzId, int score);
    List<RatingResponseDto> getRatingsByUserAndArtist(User user, String audioDbId);
    Page<ArtistDto> findArtistsRatedByUser(User user, Pageable pageable);
    void saveRatingsInBulk(User user, List<RatingDto> newRatings);
}
