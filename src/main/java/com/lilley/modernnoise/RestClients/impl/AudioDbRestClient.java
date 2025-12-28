package com.lilley.modernnoise.RestClients.impl;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Dtos.ResponseDtos.AlbumSearchResponse;
import com.lilley.modernnoise.Data.Dtos.ResponseDtos.ArtistSearchResponse;
import com.lilley.modernnoise.Data.Entities.Artist;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class AudioDbRestClient {
    private final RestClient restClient;

    public AudioDbRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ArtistDto> FetchArtistData(String artistName) {
        String extension = "123/search.php";
        var response = restClient.get()
                .uri(uri -> uri.path(extension)
                        .queryParam("s", artistName)
                        .build())
                .retrieve().body(ArtistSearchResponse.class);

        if (response == null || response.artists() == null || response.artists().isEmpty()) {
            return null;
        }

        return response.artists();
    }

    public List<AlbumDto> FetchAlbumsByArtist(String artistName) {
        String extension = "123/searchalbum.php";
        var response = restClient.get()
                .uri(uri -> uri.path(extension)
                        .queryParam("s", artistName)
                        .build())
                .retrieve().body(AlbumSearchResponse.class);
        if (response == null || response.album() == null || response.album().isEmpty()) {
            return null;
        }

        return response.album();
    }


}
