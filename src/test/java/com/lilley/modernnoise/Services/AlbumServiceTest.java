package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Entities.Album;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Repos.AlbumRepo;
import com.lilley.modernnoise.Repos.ArtistRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepo albumRepo;

    @Mock
    private ArtistRepo artistRepo;

    @InjectMocks
    private AlbumService albumService;

    private Album album;
    private final UUID artistId = UUID.randomUUID();
    private final String audioDbId = "album123";

    @BeforeEach
    void setUp() {
        album = Album.builder()
                .title("Abbey Road")
                .releaseYear("1969")
                .artistName("The Beatles")
                .audioDbId(audioDbId)
                .build();
    }

    @Test
    void getAlbumsByArtistId_ShouldReturnAlbumDtoList() {
        when(albumRepo.findByArtistIdOrderByReleaseYear(artistId)).thenReturn(List.of(album));

        List<AlbumDto> result = albumService.getAlbumsByArtistId(artistId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Abbey Road", result.get(0).strAlbum());
        verify(albumRepo).findByArtistIdOrderByReleaseYear(artistId);
    }

    @Test
    void getAlbumMusicBrainzId_ShouldReturnAlbum_WhenFound() {
        when(albumRepo.findByAudioDbId(audioDbId)).thenReturn(Optional.of(album));

        Optional<Album> result = albumService.getAlbumMusicBrainzId(audioDbId);

        assertTrue(result.isPresent());
        assertEquals("Abbey Road", result.get().getTitle());
        verify(albumRepo).findByAudioDbId(audioDbId);
    }

    @Test
    void getAlbumsByMusicBrainzId_ShouldReturnSortedAlbumDtoList_WhenArtistFound() {
        String artistMusicBrainzId = "artist123";
        Artist artist = mock(Artist.class);
        Album album2 = Album.builder()
                .title("Revolver")
                .releaseYear("1966")
                .artistName("The Beatles")
                .audioDbId("album456")
                .build();

        when(artistRepo.findByAudioDbId(artistMusicBrainzId)).thenReturn(artist);
        when(artist.getAlbums()).thenReturn(List.of(album, album2));

        List<AlbumDto> result = albumService.getAlbumsByMusicBrainzId(artistMusicBrainzId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Revolver", result.get(0).strAlbum()); // 1966 comes before 1969
        assertEquals("Abbey Road", result.get(1).strAlbum());
        verify(artistRepo).findByAudioDbId(artistMusicBrainzId);
    }

    @Test
    void getAlbumsByMusicBrainzId_ShouldReturnEmptyList_WhenArtistNotFound() {
        String artistMusicBrainzId = "unknown";
        when(artistRepo.findByAudioDbId(artistMusicBrainzId)).thenReturn(null);

        List<AlbumDto> result = albumService.getAlbumsByMusicBrainzId(artistMusicBrainzId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(artistRepo).findByAudioDbId(artistMusicBrainzId);
    }
}
