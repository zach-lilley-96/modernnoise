package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Repos.AlbumRepo;
import com.lilley.modernnoise.Repos.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepo albumRepo;
    private final ArtistRepo artistRepo;

    public List<AlbumDto> getAlbumsByArtistId(UUID artistId) {
        return albumRepo.findByArtistIdOrderByReleaseYear(artistId)
                .stream()
                .map(AlbumMapper::toDto)
                .toList();
    }

    public List<AlbumDto> getAlbumsByMusicBrainzId(String audioDbId) {
        var artist = artistRepo.findByAudioDbId(audioDbId);
        if (artist == null) {
            return List.of();
        }

        return artist.getAlbums().stream()
                .map(AlbumMapper::toDto)
                .sorted(Comparator.comparing(AlbumDto::intYearReleased))
                .toList();
    }
}
