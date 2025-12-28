package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import com.lilley.modernnoise.Services.AudioDbService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("audiodb")
public class AudioDbController {
    private final AudioDbService audioDbService;

    public AudioDbController(AudioDbService audioDbService) {
        this.audioDbService = audioDbService;
    }


    @GetMapping("/artist/{artistName}")
    public List<ArtistDto> searchArtistByName(@PathVariable String artistName) {
        return audioDbService.GetArtistData(artistName);
    }

    @GetMapping("/albums/{artistName}")
    public List<AlbumDto> getAlbumsByArtistName(@PathVariable String artistName) {
        return audioDbService.GetAlbumsByArtist(artistName);
    }
}
