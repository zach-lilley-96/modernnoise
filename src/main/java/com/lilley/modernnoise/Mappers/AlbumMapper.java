package com.lilley.modernnoise.Mappers;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Entities.Album;

public class AlbumMapper {
    private AlbumMapper(){

    }

    public static AlbumDto toDto(Album album){
        return new AlbumDto(
                album.getTitle(),
                album.getReleaseYear(),
                album.getThumbnailUrl(),
                album.getArtistName(),
                album.getAudioDbId()
        );
    }

    public static Album toEntity(AlbumDto dto){
        return Album.builder()
                .artistName(dto.strArtist())
                .audioDbId(dto.strMusicBrainzID())
                .releaseYear(dto.intYearReleased())
                .thumbnailUrl(dto.strAlbumThumb())
                .title(dto.strAlbum())
                .build();

    }
}
