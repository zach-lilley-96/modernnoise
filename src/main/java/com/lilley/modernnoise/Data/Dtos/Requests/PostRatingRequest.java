package com.lilley.modernnoise.Data.Dtos.Requests;

import com.lilley.modernnoise.Data.Dtos.RatingDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostRatingRequest(
        @NotNull List<RatingDto> ratings
) {
}
