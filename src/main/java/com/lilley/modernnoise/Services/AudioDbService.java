package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioDbService {
    private final AudioDbRestClient client;

    public List<ArtistDto> GetArtistData(String artistName) {
        return client.FetchArtistData(artistName);
    }

    public List<AlbumDto> GetAlbumsByArtist(String artistName) {
        return client.FetchAlbumsByArtist(artistName);
    }
}
