package com.lilley.modernnoise.Data.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String releaseYear;

    @NotBlank
    @Column(nullable = false)
    private String artistName;

    @Column(nullable = true)
    private String thumbnailUrl;

    @Column(unique = true, updatable = false)
    private String audioDbId;
}
