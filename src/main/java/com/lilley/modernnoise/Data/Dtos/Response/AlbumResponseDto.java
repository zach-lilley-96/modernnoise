package com.lilley.modernnoise.Data.Dtos.Response;

import java.util.UUID;

public record AlbumResponseDto(
        UUID id,
        String title,
        String releaseYear
) {
}
