package com.lilley.modernnoise.Mappers;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;

public class ArtistMapper {

    private ArtistMapper() {

    }

    public static Artist toEntity(ArtistDto dto){
        return Artist.builder()
                .name(dto.strArtist())
                .genre(dto.strGenre())
                .thumbnailUrl(dto.strArtistThumb())
                .formedYear(dto.intFormedYear())
                .build();
    }

    public static ArtistDto toDto(Artist artist){
        return new ArtistDto(
                artist.getName(),
                artist.getGenre(),
                artist.getThumbnailUrl(),
                artist.getFormedYear()
        );
    }
}
