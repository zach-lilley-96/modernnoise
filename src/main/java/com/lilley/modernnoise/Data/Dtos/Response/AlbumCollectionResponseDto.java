package com.lilley.modernnoise.Data.Dtos.Response;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;

import java.util.List;

public record AlbumCollectionResponseDto(
        List<AlbumDto> albums
) {
}
