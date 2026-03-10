package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.Mail.EmailDetails;
import com.lilley.modernnoise.Data.Dtos.Mail.MailType;
import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Dtos.Response.RatingResponseDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Mappers.RatingMapper;
import com.lilley.modernnoise.Repos.RatingRepo;
import com.lilley.modernnoise.Services.Interfaces.IRatingService;
import com.lilley.modernnoise.Services.Interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService implements IRatingService {
    private final RatingRepo ratingRepo;
    private final AlbumService albumService;
    private final MailService mailService;

    @Override
    @Transactional
    public Optional<RatingDto> rateAlbum(User user, String albumMusicBrainzId, int score) {
        log.info("User {} rating album {} with score {}", user.getEmail(), albumMusicBrainzId, score);
        validateScore(score);
        var albumExists = albumService.getAlbumMusicBrainzId(albumMusicBrainzId);
        if (albumExists.isEmpty()) {
            log.warn("Album {} not found for rating by user {}", albumMusicBrainzId, user.getEmail());
            return Optional.empty();
        }
        Album album = albumExists.get();
        Optional<Rating> existingRating = ratingRepo.findByUserAndAlbum(user, album);

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            log.info("Updating existing rating for user {} and album {} from {} to {}", user.getEmail(), albumMusicBrainzId, rating.getScore(), score);
            rating.setScore(score);
            ratingRepo.save(rating);
            return Optional.of(RatingMapper.toDto(rating));
        } else {
            log.info("Creating new rating for user {} and album {} with score {}", user.getEmail(), albumMusicBrainzId, score);

            boolean isNewArtist = !ratingRepo.existsByUserAndArtist(user, album.getArtist());

            Rating newRating =
                    Rating.builder()
                            .user(user)
                            .album(album)
                            .score(score)
                            .build();
            ratingRepo.save(newRating);

            if (isNewArtist) {
                notifyFriendsOfNewArtist(user, album.getArtist());
            }

            return Optional.of(RatingMapper.toDto(newRating));
        }
    }

    private void notifyFriendsOfNewArtist(User user, Artist artist) {
        Set<User> friends = user.getFriendSet();
        if (friends == null || friends.isEmpty()) {
            log.debug("User {} has no friends to notify about new artist {}", user.getEmail(), artist.getName());
            return;
        }

        log.info("Notifying {} friends of user {} about new artist {}", friends.size(), user.getEmail(), artist.getName());
        for (User friend : friends) {
            String subject = String.format("%s just rated a new artist!", user.getDisplayName());
            String body = String.format("Your friend %s just rated a new artist: %s! Check out what they think of it.",
                                        user.getDisplayName(), artist.getName());
            EmailDetails email = new EmailDetails(friend.getEmail(), subject, body, MailType.NOTIFICATION);
            mailService.sendEmail(email);
        }
    }

    @Override
    @Transactional
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
                .collect(Collectors.toMap(r -> r.getAlbum().getAudioDbId(), r -> r));

        List<Rating> ratingsToSave = new ArrayList<>();
        Set<Artist> newArtistsRated = new HashSet<>();

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

                if (!ratingRepo.existsByUserAndArtist(user, album.getArtist())) {
                    newArtistsRated.add(album.getArtist());
                }
            }
            ratingsToSave.add(rating);
        }

        ratingRepo.saveAll(ratingsToSave);
        log.info("Successfully saved {} ratings in bulk for user {}", ratingsToSave.size(), user.getEmail());

        for (Artist artist : newArtistsRated) {
            notifyFriendsOfNewArtist(user, artist);
        }
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

    @Override
    public Page<ArtistDto> searchUserSavedArtists(User user, String searchTerm, Pageable pageable) {
        if (searchTerm.isBlank()) {
            return Page.empty();
        }
        return ratingRepo.searchUserSavedArtists( user, searchTerm, pageable).map(ArtistMapper::toDto);
    }

}
