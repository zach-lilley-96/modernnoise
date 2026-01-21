package com.lilley.modernnoise.Data.Dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingDto(
        String albumId,
        @Min(1) @Max(10)
        float score
) {
}
