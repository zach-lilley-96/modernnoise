package com.lilley.modernnoise.Data.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArtistDto(
        String strArtist,
        String strGenre,
        String strArtistThumb,
        String strMusicBrainzID,
        int intFormedYear
) {
}
