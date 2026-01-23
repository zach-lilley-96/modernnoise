package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Services.AlbumService;
import com.lilley.modernnoise.Services.ArtistService;
import com.lilley.modernnoise.Services.AudioDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Change for production env
@RestController
@Slf4j
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final AudioDbService audioDbService;
    private final ArtistService artistService;
    private final AlbumService albumService;

    @GetMapping("/artist/{artistName}")
    public List<ArtistDto> searchArtistByName(@PathVariable String artistName) {
        log.info("Searching for artist: {}", artistName);
        var artists = audioDbService.GetArtistData(artistName);
        log.info("Found {} artists for search term: {}", artists.size(), artistName);
        return artists;
    }

    @GetMapping("/albums/{artistAudioDbId}")
    public ResponseEntity<List<AlbumDto>> getAlbumsByArtistName(@PathVariable String artistAudioDbId) {
        log.info("Fetching albums for artist AudioDbId: {}", artistAudioDbId);
        var existingAlbums = albumService.getAlbumsByMusicBrainzId(artistAudioDbId);
        if (!existingAlbums.isEmpty()) {
            log.info("Found {} existing albums in database for artist AudioDbId: {}", existingAlbums.size(), artistAudioDbId);
            return ResponseEntity.ok(existingAlbums);
        }
        try{
            log.info("No existing albums found, fetching from AudioDb for artist AudioDbId: {}", artistAudioDbId);
            var remoteAlbums = audioDbService.GetAlbumsByArtist(artistAudioDbId);
            log.info("Found {} albums from AudioDb for artist AudioDbId: {}", remoteAlbums.size(), artistAudioDbId);
            return ResponseEntity.ok(remoteAlbums);
        } catch (Exception e){
            log.error("Error fetching albums for artist AudioDbId: {}", artistAudioDbId, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
