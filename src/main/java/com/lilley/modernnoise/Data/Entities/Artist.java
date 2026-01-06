package com.lilley.modernnoise.Data.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String genre;

    @NotBlank
    @Column(length = 1000)
    private String thumbnailUrl;

    @Column(unique = true, updatable = false)
    private String audioDbId;

    @Column(updatable = false)
    private int formedYear;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Album> albums = new ArrayList<>();

    @ManyToMany(mappedBy = "savedArtists")
    private Set<User> savedByUsers = new HashSet<>();

    public void addAlbum(Album album) {
        albums.add(album);
        album.setArtist(this);
    }

}
