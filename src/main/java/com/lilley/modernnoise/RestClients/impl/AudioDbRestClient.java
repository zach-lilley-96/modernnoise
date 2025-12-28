package com.lilley.modernnoise.RestClients.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AudioDbRestClient {
    private final RestClient restClient;

    public AudioDbRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void GetArtistData(String artistName) {
        String extension = "123/search.php";
        var response = restClient.get()
                .uri(uri -> uri.path(extension)
                        .queryParam("s", artistName)
                        .build())
                .retrieve().body(String.class);
        System.out.println(response);
    }


}
