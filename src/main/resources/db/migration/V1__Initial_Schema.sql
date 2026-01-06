CREATE TABLE artist (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(500) NOT NULL,
    thumbnail_url VARCHAR(1000) NOT NULL,
    audio_db_id VARCHAR(255) UNIQUE,
    formed_year INT
);

CREATE TABLE album (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_year VARCHAR(255) NOT NULL,
    artist_name VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255),
    audio_db_id VARCHAR(255) UNIQUE,
    artist_id UUID NOT NULL,
    CONSTRAINT fk_album_artist FOREIGN KEY (artist_id) REFERENCES artist(id)
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255),
    provider VARCHAR(255) NOT NULL
);
