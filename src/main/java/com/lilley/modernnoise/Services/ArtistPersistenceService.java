package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Mappers.AlbumMapper;
import com.lilley.modernnoise.Mappers.ArtistMapper;
import com.lilley.modernnoise.Repos.AlbumRepo;
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
    private final AlbumRepo albumRepo;
    private final AudioDbRestClient audioDbRestClient;

    @Async
    @Transactional
    public void persistArtistDataAsync(ArtistDto artistDto){
        if (artistRepo.existsByAudioDbId(artistDto.strMusicBrainzID())) {
            return;
        }
        Artist newArtist = ArtistMapper.toEntity(artistDto);
        artistRepo.save(newArtist);

        var albums = audioDbRestClient.FetchAlbumsByArtist(artistDto.strArtist());
        if (albums != null){
            albums.forEach(a -> albumRepo.save(AlbumMapper.toEntity(a)));
        }
    }
}
