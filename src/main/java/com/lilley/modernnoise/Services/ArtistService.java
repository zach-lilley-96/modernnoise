package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Repos.ArtistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepo artistRepo;

    public boolean artistExistsByName(String artistName) {
        return artistRepo.existsByName(artistName);
    }

    public boolean artistExistsByAudioDbId(String audioDbId) {
        return artistRepo.existsByAudioDbId(audioDbId);
    }

    public ArtistDto getArtistByName(String artistName) {
        var artist = artistRepo.findByNameIgnoreCase(artistName);
        return artist.map(ArtistMapper::toDto).orElse(null);
    }

//    public ArtistDto createArtist(ArtistDto artistDto) {
//        var artistExists = artistExistsByAudioDbId(artistDto.strMusicBrainzID());
//        if (artistExists) {
//            return ArtistMapper.toDto(artistRepo.findByAudioDbId(artistDto.strMusicBrainzID()));
//        }
//        var artist = ArtistMapper.toEntity(artistDto);
//        var savedArtist = artistRepo.save(artist);
//        return ArtistMapper.toDto(savedArtist);
//    }

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
