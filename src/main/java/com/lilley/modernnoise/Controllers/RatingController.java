package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.Requests.PostRatingRequest;
import com.lilley.modernnoise.Data.Dtos.Response.RatingResponseDto;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Services.FriendService;
import com.lilley.modernnoise.Services.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final FriendService friendService;

    @GetMapping("/artists")
    public Page<ArtistDto> getArtistsRatedByUser(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        log.info("Fetching artists rated by user: {} (page: {}, size: {})", user.getEmail(), page, size);
        var artists = ratingService.findArtistsRatedByUser(user, PageRequest.of(page, size));
        log.info("Found {} artists rated by user: {}", artists.getTotalElements(), user.getEmail());
        return artists;
    }

    @GetMapping("/friend")
    public Page<ArtistDto> getArtistsRatedByFriend(
            @AuthenticationPrincipal User user,
            @RequestParam String friendCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        var friendUser = friendService.GetUserByFriendCode(UUID.fromString(friendCode));
        log.info("Fetching artists rated by friend of user: {} (page: {}, size: {})", user.getEmail(), page, size);
        return ratingService.findArtistsRatedByUser(friendUser, PageRequest.of(page, size));
    }

    @GetMapping("/my-ratings/{audioDbId}")
    public List<RatingResponseDto> getMyRatingsForArtist(@AuthenticationPrincipal User user, @PathVariable String audioDbId) {
        log.info("Fetching ratings for artist {} by user {}", audioDbId, user.getEmail());
        var ratings = ratingService.getRatingsByUserAndArtist(user, audioDbId);
        log.info("Found {} ratings for artist {} by user {}", ratings.size(), audioDbId, user.getEmail());
        return ratings;
    }

    @GetMapping("/friend-ratings/{audioDbId}")
    public List<RatingResponseDto> getFriendRatingsForArtist(
            @AuthenticationPrincipal User user,
            @PathVariable String audioDbId,
            @RequestParam String friendCode
    ) {
        var friendUser = friendService.GetUserByFriendCode(UUID.fromString(friendCode));
        log.info("Fetching ratings for artist {} by friend of user {}", audioDbId, user.getEmail());
        var ratings = ratingService.getRatingsByUserAndArtist(friendUser, audioDbId);
        log.info("Found {} ratings for artist {} by friend of user {}", ratings.size(), audioDbId, user.getEmail());
        return ratings;
    }

    @PostMapping("/save")
    public ResponseEntity<?> postRating(@AuthenticationPrincipal User user, @RequestBody PostRatingRequest request) {
        log.info("Received rating request for user {} with {} ratings", user.getEmail(), request.ratings().size());
        ratingService.saveRatingsInBulk(user, request.ratings());
        log.info("Successfully saved bulk ratings for user {}", user.getEmail());
        return ResponseEntity.ok().build();
    }

}
