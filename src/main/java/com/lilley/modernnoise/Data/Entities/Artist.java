package com.lilley.modernnoise.Data.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Artist {
    @Id
    @GeneratedValue
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
    private int formedYear = 9999;

}
