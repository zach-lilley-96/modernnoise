package com.lilley.modernnoise.Data.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlbumDto(
        String strAlbum,
        String intYearReleased,
        String strAlbumThumb,
        String strArtist
) {}
