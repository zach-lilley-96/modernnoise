package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Repos.ArtistRepo;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistPersistenceService {
    private final ArtistRepo artistRepo;
    private final AudioDbRestClient audioDbRestClient;

    @Async
    @Transactional
    public void persistArtistDataAsync(ArtistDto artistDto){
        if (artistRepo.existsByAudioDbId(artistDto.strMusicBrainzID())) {
            log.info("Artist {} already exists in database, skipping persistence.", artistDto.strArtist());
            return;
        }
        log.info("Persisting artist data for: {}", artistDto.strArtist());
        Artist newArtist = ArtistMapper.toEntity(artistDto);
        var albums = audioDbRestClient.FetchAlbumsByArtist(artistDto.strArtist());
        if (albums.isEmpty()){
            log.error("No albums found for artist: {} during persistence", artistDto.strArtist());
            throw new IllegalStateException("No albums found for artist: " + artistDto.strArtist());
        }
        log.info("Found {} albums for artist: {}. Filtering and adding to entity.", albums.size(), artistDto.strArtist());
        albums.stream()
                .filter(album -> isNotStandardLP(album.strAlbum()))
                .map(AlbumMapper::toEntity)
                .forEach(newArtist::addAlbum);
        artistRepo.save(newArtist);
        log.info("Successfully persisted artist {} and their albums.", artistDto.strArtist());
    }

    private boolean isNotStandardLP(String albumName){
        return !albumName.toLowerCase().contains("best of")
                && !albumName.toLowerCase().contains("live")
                && !albumName.toLowerCase().contains("single")
                && !albumName.toLowerCase().contains("remix")
                && !albumName.toLowerCase().contains("remaster");
    }
}
