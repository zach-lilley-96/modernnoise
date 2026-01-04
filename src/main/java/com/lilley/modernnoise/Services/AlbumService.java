package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Repos.AlbumRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepo albumRepo;

    public List<AlbumDto> getAlbumsByArtistId(UUID artistId) {
        return albumRepo.findByArtistIdOrderByReleaseYear(artistId)
                .stream()
                .map(AlbumMapper::toDto)
                .toList();
    }
}
