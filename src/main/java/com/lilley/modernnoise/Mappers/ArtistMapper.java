package com.lilley.modernnoise.Mappers;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;
import org.jspecify.annotations.NonNull;

public class ArtistMapper{

    private ArtistMapper() {

    }

    public static Artist toEntity(@NonNull ArtistDto dto){
        return Artist.builder()
                .name(dto.strArtist())
                .genre(dto.strGenre())
                .thumbnailUrl(dto.strArtistThumb())
                .formedYear(dto.intFormedYear())
                .audioDbId(dto.strMusicBrainzID())
                .build();
    }

    public static ArtistDto toDto(@NonNull Artist artist){
        return new ArtistDto(
                artist.getName(),
                artist.getGenre(),
                artist.getThumbnailUrl(),
                artist.getAudioDbId(),
                artist.getFormedYear()
        );
    }

}
