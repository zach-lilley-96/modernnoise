CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    album_id UUID NOT NULL,
    score INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_rating_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_rating_album FOREIGN KEY (album_id) REFERENCES album(id),
    CONSTRAINT unique_user_album_rating UNIQUE (user_id, album_id)
);

CREATE TABLE user_saved_artists (
    user_id UUID NOT NULL,
    artist_id UUID NOT NULL,
    PRIMARY KEY (user_id, artist_id),
    CONSTRAINT fk_usa_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_usa_artist FOREIGN KEY (artist_id) REFERENCES artist(id)
);
