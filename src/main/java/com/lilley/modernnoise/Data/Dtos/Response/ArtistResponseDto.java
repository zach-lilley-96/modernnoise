package com.lilley.modernnoise.Data.Dtos.Response;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;

import java.util.List;
import java.util.UUID;

public record ArtistResponseDto(
        UUID id,
        String name,
        String genre,
        String thumbnailUrl,
        List<AlbumDto> albums

) {
}
