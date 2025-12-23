package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("audiodb")
public class AudioDbController {
    private final AudioDbRestClient audioDbRestClient;

    public AudioDbController(AudioDbRestClient audioDbRestClient) {
        this.audioDbRestClient = audioDbRestClient;
    }

    @GetMapping("/{artistName}")
    public void testAudioDb(@PathVariable String artistName) {
        audioDbRestClient.GetArtistData(artistName);
    }
}
