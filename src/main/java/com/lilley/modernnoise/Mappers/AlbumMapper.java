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
                album.getArtistName()
        );
    }

    public static Album toEntity(AlbumDto dto){
        Album album = new Album();
        album.setTitle(dto.strAlbumStripped());
        album.setReleaseYear(dto.intYearReleased());
        album.setThumbnailUrl(dto.strAlbumThumb());
        album.setArtistName(dto.strArtistStripped());
        return album;
    }
}
