package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudioDbService {
    private final AudioDbRestClient client;

    public AudioDbService(AudioDbRestClient client) {
        this.client = client;
    }

    public List<ArtistDto> GetArtistData(String artistName) {
        return client.FetchArtistData(artistName);
    }

    public List<AlbumDto> GetAlbumsByArtist(String artistName) {
        return client.FetchAlbumsByArtist(artistName);
    }
}
