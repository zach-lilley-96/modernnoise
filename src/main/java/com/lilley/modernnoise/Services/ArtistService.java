package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Repos.ArtistRepo;
import com.lilley.modernnoise.Services.Interfaces.IArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService implements IArtistService {
    private final ArtistRepo artistRepo;

    public boolean artistExistsByName(String artistName) {
        log.info("Checking if artist exists by name: {}", artistName);
        return artistRepo.existsByName(artistName);
    }

    public boolean artistExistsByAudioDbId(String audioDbId) {
        log.info("Checking if artist exists by AudioDb ID: {}", audioDbId);
        return artistRepo.existsByAudioDbId(audioDbId);
    }

    public ArtistDto getArtistByName(String artistName) {
        log.info("Fetching artist by name: {}", artistName);
        var artist = artistRepo.findByNameIgnoreCase(artistName);
        if (artist.isEmpty()) {
            log.warn("Artist not found by name: {}", artistName);
        }
        return artist.map(ArtistMapper::toDto).orElse(null);
    }

}
