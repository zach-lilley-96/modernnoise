package com.lilley.modernnoise.Data.Dtos.Response;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;

public record RatingResponseDto(
    AlbumDto album,
    String displayName,
    float score
) {
}
