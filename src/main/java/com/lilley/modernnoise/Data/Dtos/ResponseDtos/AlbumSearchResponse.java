package com.lilley.modernnoise.Data.Dtos.ResponseDtos;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;

import java.util.List;

public record AlbumSearchResponse(List<AlbumDto> albums) {
}
