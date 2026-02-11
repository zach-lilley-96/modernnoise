package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.ArtistDto;
import com.lilley.modernnoise.Data.Entities.Artist;
import com.lilley.modernnoise.Repos.ArtistRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepo artistRepo;

    @InjectMocks
    private ArtistService artistService;

    private Artist artist;
    private final String artistName = "The Beatles";
    private final String audioDbId = "123";

    @BeforeEach
    void setUp() {
        artist = Artist.builder()
                .name(artistName)
                .genre("Rock")
                .thumbnailUrl("thumb.jpg")
                .audioDbId(audioDbId)
                .formedYear(1960)
                .build();
    }

    @Test
    void artistExistsByName_ShouldReturnTrue_WhenExists() {
        when(artistRepo.existsByName(artistName)).thenReturn(true);

        boolean exists = artistService.artistExistsByName(artistName);

        assertTrue(exists);
        verify(artistRepo).existsByName(artistName);
    }

    @Test
    void artistExistsByName_ShouldReturnFalse_WhenNotExists() {
        when(artistRepo.existsByName(artistName)).thenReturn(false);

        boolean exists = artistService.artistExistsByName(artistName);

        assertFalse(exists);
        verify(artistRepo).existsByName(artistName);
    }

    @Test
    void artistExistsByAudioDbId_ShouldReturnTrue_WhenExists() {
        when(artistRepo.existsByAudioDbId(audioDbId)).thenReturn(true);

        boolean exists = artistService.artistExistsByAudioDbId(audioDbId);

        assertTrue(exists);
        verify(artistRepo).existsByAudioDbId(audioDbId);
    }

    @Test
    void artistExistsByAudioDbId_ShouldReturnFalse_WhenNotExists() {
        when(artistRepo.existsByAudioDbId(audioDbId)).thenReturn(false);

        boolean exists = artistService.artistExistsByAudioDbId(audioDbId);

        assertFalse(exists);
        verify(artistRepo).existsByAudioDbId(audioDbId);
    }

    @Test
    void getArtistByName_ShouldReturnArtistDto_WhenFound() {
        when(artistRepo.findByNameIgnoreCase(artistName)).thenReturn(Optional.of(artist));

        ArtistDto result = artistService.getArtistByName(artistName);

        assertNotNull(result);
        assertEquals(artistName, result.strArtist());
        assertEquals(audioDbId, result.strMusicBrainzID());
        verify(artistRepo).findByNameIgnoreCase(artistName);
    }

    @Test
    void getArtistByName_ShouldReturnNull_WhenNotFound() {
        when(artistRepo.findByNameIgnoreCase(artistName)).thenReturn(Optional.empty());

        ArtistDto result = artistService.getArtistByName(artistName);

        assertNull(result);
        verify(artistRepo).findByNameIgnoreCase(artistName);
    }
}
