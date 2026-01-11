package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Mappers.RatingMapper;
import com.lilley.modernnoise.Repos.RatingRepo;
import com.lilley.modernnoise.Services.Interfaces.IRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {
    private final RatingRepo ratingRepo;
    private final AlbumService albumService;

    public Optional<RatingDto> rateAlbum(User user, String albumMusicBrainzId, int score) {
        validateScore(score);
        var albumExists = albumService.getAlbumMusicBrainzId(albumMusicBrainzId);
        if (albumExists.isEmpty()) {
            return Optional.empty();
        }
        Optional<Rating> existingRating = ratingRepo.findByUserAndAlbum(user, albumExists.get());

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setScore(score);
            ratingRepo.save(rating);
            return Optional.of(RatingMapper.toDto(rating));
        } else {
            Rating newRating =
                    Rating.builder()
                            .user(user)
                            .album(albumExists.get())
                            .score(score)
                            .build();
            ratingRepo.save(newRating);
            return Optional.of(RatingMapper.toDto(newRating));
        }
    }

    @Override
    public List<RatingDto> getRatingsByUserAndArtist(User user, UUID artistId) {
        return ratingRepo.findByUserAndArtist(user, artistId).stream()
                .map(RatingMapper::toDto)
                .toList();
    }

    private void validateScore(int score) {
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Score must be between 1 and 10");
        }
    }
}
