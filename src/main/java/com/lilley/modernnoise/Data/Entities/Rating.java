package com.lilley.modernnoise.Data.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "ratings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "album_id"})
        }
)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(nullable = false)
    @jakarta.validation.constraints.Min(1)
    @jakarta.validation.constraints.Max(10)
    private float score; // 1–10

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating rating)) return false;
        return id != null && id.equals(rating.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
