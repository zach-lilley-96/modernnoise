package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import com.lilley.modernnoise.Services.Interfaces.IAudioDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AudioDbService implements IAudioDbService {
    private final AudioDbRestClient client;
    private final ArtistPersistenceService artistPersistenceService;

    public List<ArtistDto> GetArtistData(String artistName) {
        log.info("Fetching artist data from AudioDb for: {}", artistName);
        var artists = client.FetchArtistData(artistName);
        if (artists == null || artists.isEmpty()) {
            log.warn("No artist data found in AudioDb for: {}", artistName);
            return List.of();
        }
        log.info("Found {} artists in AudioDb for: {}. Persisting data asynchronously.", artists.size(), artistName);
        artists.forEach(
                artistPersistenceService::persistArtistDataAsync
        );
        return artists;
    }

    public List<AlbumDto> GetAlbumsByArtist(String artistName) {
        log.info("Fetching albums from AudioDb for artist: {}", artistName);
        var albums = client.FetchAlbumsByArtist(artistName);
        log.info("Found {} albums in AudioDb for artist: {}", albums.size(), artistName);
        return albums;
    }
}
