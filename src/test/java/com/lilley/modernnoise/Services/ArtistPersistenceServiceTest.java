package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.AlbumDto;
import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Repos.ArtistRepo;
import com.lilley.modernnoise.RestClients.impl.AudioDbRestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistPersistenceServiceTest {

    @Mock
    private ArtistRepo artistRepo;

    @Mock
    private AudioDbRestClient audioDbRestClient;

    @InjectMocks
    private ArtistPersistenceService artistPersistenceService;

    @Test
    void persistArtistDataAsync_ShouldSkip_WhenArtistExists() {
        ArtistDto dto = new ArtistDto("Beatles", "Rock", "thumb", "123", 1960);
        when(artistRepo.existsByAudioDbId("123")).thenReturn(true);

        artistPersistenceService.persistArtistDataAsync(dto);

        verify(artistRepo, never()).save(any());
    }

    @Test
    void persistArtistDataAsync_ShouldThrowException_WhenNoAlbumsFound() {
        ArtistDto dto = new ArtistDto("Beatles", "Rock", "thumb", "123", 1960);
        when(artistRepo.existsByAudioDbId("123")).thenReturn(false);
        when(audioDbRestClient.FetchAlbumsByArtist("Beatles")).thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> artistPersistenceService.persistArtistDataAsync(dto));
    }

    @Test
    void persistArtistDataAsync_ShouldSaveArtistWithFilteredAlbums() {
        ArtistDto dto = new ArtistDto("Beatles", "Rock", "thumb", "123", 1960);
        AlbumDto album1 = new AlbumDto("Abbey Road", "1969", "thumb1", "Beatles", "a1");
        AlbumDto album2 = new AlbumDto("Live at the BBC", "1994", "thumb2", "Beatles", "a2"); // Should be filtered out
        AlbumDto album3 = new AlbumDto("Revolver", "1966", "thumb3", "Beatles", "a3");

        when(artistRepo.existsByAudioDbId("123")).thenReturn(false);
        when(audioDbRestClient.FetchAlbumsByArtist("Beatles")).thenReturn(List.of(album1, album2, album3));

        artistPersistenceService.persistArtistDataAsync(dto);

        ArgumentCaptor<Artist> artistCaptor = ArgumentCaptor.forClass(Artist.class);
        verify(artistRepo).save(artistCaptor.capture());

        Artist savedArtist = artistCaptor.getValue();
        assertEquals("Beatles", savedArtist.getName());
        assertEquals(2, savedArtist.getAlbums().size());
        assertTrue(savedArtist.getAlbums().stream().anyMatch(a -> a.getTitle().equals("Abbey Road")));
        assertTrue(savedArtist.getAlbums().stream().anyMatch(a -> a.getTitle().equals("Revolver")));
        assertFalse(savedArtist.getAlbums().stream().anyMatch(a -> a.getTitle().equals("Live at the BBC")));
    }
}
