package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Repos.ArtistRepo;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistPersistenceService {
    private final ArtistRepo artistRepo;
    private final AudioDbRestClient audioDbRestClient;

    @Async
    @Transactional
    public void persistArtistDataAsync(ArtistDto artistDto){
        if (artistRepo.existsByAudioDbId(artistDto.strMusicBrainzID())) {
            return;
        }
        Artist newArtist = ArtistMapper.toEntity(artistDto);
        var albums = audioDbRestClient.FetchAlbumsByArtist(artistDto.strArtist());
        if (albums.isEmpty()){
            throw new IllegalStateException("No albums found for artist: " + artistDto.strArtist());
        }
        albums.stream()
                .filter(album -> isNotStandardLP(album.strAlbum()))
                .map(AlbumMapper::toEntity)
                .forEach(newArtist::addAlbum);
        artistRepo.save(newArtist);

    }

    private boolean isNotStandardLP(String albumName){
        return !albumName.toLowerCase().contains("best of")
                && !albumName.toLowerCase().contains("live")
                && !albumName.toLowerCase().contains("single")
                && !albumName.toLowerCase().contains("remix")
                && !albumName.toLowerCase().contains("remaster");
    }
}
