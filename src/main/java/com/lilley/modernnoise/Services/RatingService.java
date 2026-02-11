package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Dtos.Response.RatingResponseDto;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Mappers.RatingMapper;
import com.lilley.modernnoise.Repos.RatingRepo;
import com.lilley.modernnoise.Services.Interfaces.IRatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService implements IRatingService {
    private final RatingRepo ratingRepo;
    private final AlbumService albumService;

    public Optional<RatingDto> rateAlbum(User user, String albumMusicBrainzId, int score) {
        log.info("User {} rating album {} with score {}", user.getEmail(), albumMusicBrainzId, score);
        validateScore(score);
        var albumExists = albumService.getAlbumMusicBrainzId(albumMusicBrainzId);
        if (albumExists.isEmpty()) {
            log.warn("Album {} not found for rating by user {}", albumMusicBrainzId, user.getEmail());
            return Optional.empty();
        }
        Optional<Rating> existingRating = ratingRepo.findByUserAndAlbum(user, albumExists.get());

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            log.info("Updating existing rating for user {} and album {} from {} to {}", user.getEmail(), albumMusicBrainzId, rating.getScore(), score);
            rating.setScore(score);
            ratingRepo.save(rating);
            return Optional.of(RatingMapper.toDto(rating));
        } else {
            log.info("Creating new rating for user {} and album {} with score {}", user.getEmail(), albumMusicBrainzId, score);
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

    public void saveRatingsInBulk(User user, List<RatingDto> newRatings) {
        log.info("Saving {} ratings in bulk for user {}", newRatings.size(), user.getEmail());
        var albumIds = newRatings.stream()
                .map(RatingDto::albumId)
                .toList();

        var existingRatings = ratingRepo.findByUserAndAlbumIds(user, albumIds).orElseThrow(() -> {
            log.error("Failed to find existing ratings for user {}", user.getEmail());
            return new RuntimeException("No ratings found for user");
        });

        var ratingsMap = existingRatings.stream()
                .collect(java.util.stream.Collectors.toMap(r -> r.getAlbum().getAudioDbId(), r -> r));

        List<Rating> ratings = new ArrayList<>();

        for (RatingDto ratingDto : newRatings) {
            Rating rating;
            if (ratingsMap.containsKey(ratingDto.albumId())) {
                rating = ratingsMap.get(ratingDto.albumId());
                rating.setScore(ratingDto.score());
            } else {
                var album = albumService.getAlbumMusicBrainzId(ratingDto.albumId()).orElseThrow(
                        () -> {
                            log.error("Album not found during bulk save: {} for user {}", ratingDto.albumId(), user.getEmail());
                            return new IllegalArgumentException("Album not found: " + ratingDto.albumId());
                        }
                );

                rating = Rating.builder()
                        .user(user)
                        .album(album)
                        .score(ratingDto.score())
                        .build();
            }
            ratings.add(rating);
        }

        ratingRepo.saveAll(ratings);
        log.info("Successfully saved {} ratings in bulk for user {}", ratings.size(), user.getEmail());
    }

    @Override
    public List<RatingResponseDto> getRatingsByUserAndArtist(User user, String audioDbId) {
        var albums = albumService.getAlbumsByMusicBrainzId(audioDbId);
        var ratings = ratingRepo.findByUserAndArtistAudioDbId(user, audioDbId).stream()
                .map(RatingMapper::toDto)
                .toList();
        log.info("Retrieved {} ratings for user {} and artist {}", ratings.size(), user.getEmail(), audioDbId);
        log.info("Found {} albums for artist {}", albums.size(), audioDbId);
        return ratings.stream()
                .map(ratingDto -> {
                    var albumDto = albums.stream()
                            .filter(album -> album.strMusicBrainzID().equals(ratingDto.albumId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Album not found for rating"));
                    return new RatingResponseDto(
                            albumDto,
                            user.getDisplayName(),
                            ratingDto.score()
                    );
                })
                .sorted(Comparator.comparing(RatingResponseDto::score).reversed())
                .toList();
    }

    private void validateScore(int score) {
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Score must be between 1 and 10");
        }
    }

    @Override
    public Page<ArtistDto> findArtistsRatedByUser(User user, Pageable pageable) {
        return ratingRepo.findDistinctArtistsRatedByUser(user, pageable).map(ArtistMapper::toDto);
    }

}
