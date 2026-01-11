package com.lilley.modernnoise.Services.Interfaces;

import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Mappers.RatingMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRatingService {
    Optional<RatingDto> rateAlbum(User user, String albumMusicBrainzId, int score);
    List<RatingDto> getRatingsByUserAndArtist(User user, UUID artistId);
}
