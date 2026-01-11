package com.lilley.modernnoise.Services.Interfaces;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;

public interface IArtistService {
    boolean artistExistsByName(String artistName);
    boolean artistExistsByAudioDbId(String audioDbId);
    ArtistDto getArtistByName(String artistName);
}
