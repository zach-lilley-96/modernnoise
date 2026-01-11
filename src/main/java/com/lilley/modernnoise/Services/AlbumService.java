package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Repos.AlbumRepo;
import com.lilley.modernnoise.Repos.ArtistRepo;
import com.lilley.modernnoise.Services.Interfaces.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumService implements IAlbumService {
    private final AlbumRepo albumRepo;
    private final ArtistRepo artistRepo;

    public List<AlbumDto> getAlbumsByArtistId(UUID artistId) {
        return albumRepo.findByArtistIdOrderByReleaseYear(artistId)
                .stream()
                .map(AlbumMapper::toDto)
                .toList();
    }

    public Optional<Album> getAlbumMusicBrainzId(String albumMusicalBrainzId){
        return albumRepo.findByAudioDbId(albumMusicalBrainzId);
    }

    public List<AlbumDto> getAlbumsByMusicBrainzId(String artistMusicBrainzId) {
        var artist = artistRepo.findByAudioDbId(artistMusicBrainzId);
        if (artist == null) {
            return List.of();
        }

        return artist.getAlbums().stream()
                .map(AlbumMapper::toDto)
                .sorted(Comparator.comparing(AlbumDto::intYearReleased))
                .toList();
    }
}
