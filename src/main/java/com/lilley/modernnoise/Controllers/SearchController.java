package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Services.ArtistService;
import com.lilley.modernnoise.Services.AudioDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final AudioDbService audioDbService;
    private final ArtistService artistService;

    @GetMapping("/artist/{artistName}")
    public List<ArtistDto> searchArtistByName(@PathVariable String artistName) {
        return audioDbService.GetArtistData(artistName);
    }

    @GetMapping("/albums/{artistAudioDbId}")
    public List<AlbumDto> getAlbumsByArtistName(@PathVariable String artistAudioDbId) {
        var existingAlbums = artistService.getAlbumsByMusicBrainzId(artistAudioDbId);
        if (!existingAlbums.isEmpty()) {
            return existingAlbums;
        }
        return audioDbService.GetAlbumsByArtist(artistAudioDbId);
    }
}
