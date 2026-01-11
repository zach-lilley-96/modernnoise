package com.lilley.modernnoise.Services.Interfaces;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;

import java.util.List;

public interface IAudioDbService {
    List<ArtistDto> GetArtistData(String artistName);

    List<AlbumDto> GetAlbumsByArtist(String artistName);
}
