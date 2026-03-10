package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.Mail.EmailDetails;
import com.lilley.modernnoise.Data.Dtos.RatingDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Data.Entities.Rating;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.RatingRepo;
import com.lilley.modernnoise.Services.Interfaces.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepo ratingRepo;

    @Mock
    private AlbumService albumService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private RatingService ratingService;

    private User user;
    private User friend;
    private Artist artist;
    private Album album;

    @BeforeEach
    void setUp() {
        friend = User.builder()
                .email("friend@example.com")
                .displayName("Friend")
                .build();

        user = User.builder()
                .email("user@example.com")
                .displayName("User")
                .friendSet(Set.of(friend))
                .build();

        artist = Artist.builder()
                .name("New Artist")
                .build();

        album = Album.builder()
                .title("New Album")
                .audioDbId("album123")
                .artist(artist)
                .build();
    }

    @Test
    void rateAlbum_ShouldNotifyFriends_WhenArtistIsNew() {
        // Arrange
        String albumId = "album123";
        int score = 8;
        when(albumService.getAlbumMusicBrainzId(albumId)).thenReturn(Optional.of(album));
        when(ratingRepo.findByUserAndAlbum(user, album)).thenReturn(Optional.empty());
        when(ratingRepo.existsByUserAndArtist(user, artist)).thenReturn(false);

        // Act
        ratingService.rateAlbum(user, albumId, score);

        // Assert
        verify(ratingRepo).save(any(Rating.class));
        verify(mailService, times(1)).sendEmail(any(EmailDetails.class));
    }

    @Test
    void rateAlbum_ShouldNotNotifyFriends_WhenArtistIsAlreadyRated() {
        // Arrange
        String albumId = "album123";
        int score = 8;
        when(albumService.getAlbumMusicBrainzId(albumId)).thenReturn(Optional.of(album));
        when(ratingRepo.findByUserAndAlbum(user, album)).thenReturn(Optional.empty());
        when(ratingRepo.existsByUserAndArtist(user, artist)).thenReturn(true);

        // Act
        ratingService.rateAlbum(user, albumId, score);

        // Assert
        verify(ratingRepo).save(any(Rating.class));
        verify(mailService, never()).sendEmail(any(EmailDetails.class));
    }

    @Test
    void rateAlbum_ShouldNotNotifyFriends_WhenUpdatingExistingRating() {
        // Arrange
        String albumId = "album123";
        int score = 8;
        Rating existingRating = Rating.builder()
                .album(album)
                .score(5)
                .build();
        when(albumService.getAlbumMusicBrainzId(albumId)).thenReturn(Optional.of(album));
        when(ratingRepo.findByUserAndAlbum(user, album)).thenReturn(Optional.of(existingRating));

        // Act
        ratingService.rateAlbum(user, albumId, score);

        // Assert
        verify(ratingRepo).save(existingRating);
        verify(mailService, never()).sendEmail(any(EmailDetails.class));
    }

    @Test
    void saveRatingsInBulk_ShouldNotifyFriendsOncePerNewArtist() {
        // Arrange
        Artist artist2 = Artist.builder().name("Artist 2").build();
        Album album2 = Album.builder().audioDbId("album2").artist(artist2).build();
        Album album3 = Album.builder().audioDbId("album3").artist(artist).build(); // Same artist as 'album'

        RatingDto dto1 = new RatingDto("album123", 8);
        RatingDto dto2 = new RatingDto("album2", 7);
        RatingDto dto3 = new RatingDto("album3", 9);

        when(ratingRepo.findByUserAndAlbumIds(eq(user), anyList())).thenReturn(Optional.of(List.of()));
        when(albumService.getAlbumMusicBrainzId("album123")).thenReturn(Optional.of(album));
        when(albumService.getAlbumMusicBrainzId("album2")).thenReturn(Optional.of(album2));
        when(albumService.getAlbumMusicBrainzId("album3")).thenReturn(Optional.of(album3));

        when(ratingRepo.existsByUserAndArtist(user, artist)).thenReturn(false);
        when(ratingRepo.existsByUserAndArtist(user, artist2)).thenReturn(false);

        // Act
        ratingService.saveRatingsInBulk(user, List.of(dto1, dto2, dto3));

        // Assert
        verify(ratingRepo).saveAll(anyList());
        // Should notify once for 'artist' and once for 'artist2'
        verify(mailService, times(2)).sendEmail(any(EmailDetails.class));
    }
}
