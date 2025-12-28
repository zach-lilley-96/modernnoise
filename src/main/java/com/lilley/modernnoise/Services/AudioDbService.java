package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import org.springframework.stereotype.Service;

@Service
public class AudioDbService {
    private final AudioDbRestClient client;

    public AudioDbService(AudioDbRestClient client) {
        this.client = client;
    }

    public void GetArtistData(String artistName) {
        String extension = "123/search.php";
        var response = client.get()
                .uri(uri -> uri.path(extension)
                        .queryParam("s", artistName)
                        .build())
                .retrieve().body(String.class);
        System.out.println(response);
    }
}
