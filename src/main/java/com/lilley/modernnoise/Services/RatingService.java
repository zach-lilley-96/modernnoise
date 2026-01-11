package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Mappers.RatingMapper;
import com.lilley.modernnoise.Repos.RatingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepo ratingRepo;

    public RatingDto rateAlbum(User user, Album album, int score) {
        validateScore(score);
        Optional<Rating> existingRating = ratingRepo.findByUserAndAlbum(user, album);

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setScore(score);
            ratingRepo.save(rating);
            return RatingMapper.toDto(rating);
        } else {
            Rating newRating = Rating.builder().user(user).album(album).score(score).build();
            ratingRepo.save(newRating);
            return RatingMapper.toDto(newRating);
        }
    }
    private void validateScore(int score) {
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Score must be between 1 and 10");
        }
    }
}
