package com.lilley.modernnoise.RestClients.impl;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.AudioDb.AlbumSearchResponse;
import com.lilley.modernnoise.Data.Dtos.AudioDb.ArtistSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AudioDbRestClient {
    private final RestClient restClient;

    public List<ArtistDto> FetchArtistData(String artistName) {
        log.info("Calling AudioDb API for artist: {}", artistName);
        String extension = "123/search.php";
        var response = restClient.get()
                .uri(uri -> uri.path(extension)
                        .queryParam("s", artistName)
                        .build())
                .retrieve().body(ArtistSearchResponse.class);

        if (response == null || response.artists() == null || response.artists().isEmpty()) {
            log.warn("AudioDb API returned no data for artist: {}", artistName);
            return null;
        }

        log.info("AudioDb API returned {} artists for: {}", response.artists().size(), artistName);
        return response.artists();
    }

    public List<AlbumDto> FetchAlbumsByArtist(String artistName) {
        log.info("Calling AudioDb API for albums by artist: {}", artistName);
        String extension = "123/searchalbum.php";
        var response = restClient.get()
                .uri(uri -> uri.path(extension)
                        .queryParam("s", artistName)
                        .build())
                .retrieve().body(AlbumSearchResponse.class);
        if (response == null || response.album() == null || response.album().isEmpty()) {
            log.warn("AudioDb API returned no albums for artist: {}", artistName);
            return null;
        }

        log.info("AudioDb API returned {} albums for artist: {}", response.album().size(), artistName);
        return response.album();
    }


}
