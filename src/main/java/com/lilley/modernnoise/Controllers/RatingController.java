package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.Requests.PostRatingRequest;
import com.lilley.modernnoise.Data.Dtos.Response.RatingResponseDto;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Services.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

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

    @GetMapping("/my-ratings/{audioDbId}")
    public List<RatingResponseDto> getMyRatingsForArtist(@AuthenticationPrincipal User user, @PathVariable String audioDbId) {
        log.info("Fetching ratings for artist {} by user {}", audioDbId, user.getEmail());
        var ratings = ratingService.getRatingsByUserAndArtist(user, audioDbId);
        log.info("Found {} ratings for artist {} by user {}", ratings.size(), audioDbId, user.getEmail());
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
