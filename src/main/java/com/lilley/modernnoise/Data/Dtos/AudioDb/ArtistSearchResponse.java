package com.lilley.modernnoise.Data.Dtos.AudioDb;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;

import java.util.List;

public record ArtistSearchResponse(List<ArtistDto> artists) {
}
