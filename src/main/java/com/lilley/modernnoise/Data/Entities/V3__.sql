CREATE TABLE ratings
(
    id         UUID      NOT NULL,
    user_id    UUID      NOT NULL,
    album_id   UUID      NOT NULL,
    score      INT       NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_ratings PRIMARY KEY (id)
);

ALTER TABLE ratings
    ADD CONSTRAINT uc_b188809c2d0b891b85905f3d9 UNIQUE (user_id, album_id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_ALBUM FOREIGN KEY (album_id) REFERENCES album (id);

ALTER TABLE ratings
    ADD CONSTRAINT FK_RATINGS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);