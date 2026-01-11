package com.lilley.modernnoise.Services.Interfaces;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;

import java.util.List;
import java.util.UUID;

public interface IAlbumService {

    List<AlbumDto> getAlbumsByArtistId(UUID artistId);
    List<AlbumDto> getAlbumsByMusicBrainzId(String audioDbId);
}
