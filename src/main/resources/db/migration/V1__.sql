CREATE SEQUENCE IF NOT EXISTS badges_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS streams_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE badges
(
    id  BIGINT NOT NULL,
    url VARCHAR(255),
    CONSTRAINT pk_badges PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id         BIGINT NOT NULL,
    text       TEXT,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    user_id    BIGINT,
    stream_id  BIGINT,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE streams
(
    id         BIGINT      NOT NULL,
    nid        VARCHAR(50) NOT NULL,
    name       VARCHAR(50),
    title      VARCHAR(255),
    category   VARCHAR(255),
    image_url  VARCHAR(255),
    avatar_url VARCHAR(255),
    video_id   VARCHAR(255),
    viewers    BIGINT,
    CONSTRAINT pk_streams PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT       NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255),
    role     VARCHAR(50),
    color    VARCHAR(7),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_badges
(
    badge_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL
);

ALTER TABLE streams
    ADD CONSTRAINT uc_streams_nid UNIQUE (nid);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

CREATE UNIQUE INDEX idx_users_username ON users (username);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_STREAM FOREIGN KEY (stream_id) REFERENCES streams (id);

CREATE INDEX idx_messages_stream_id ON messages (stream_id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_messages_user_id ON messages (user_id);

ALTER TABLE users_badges
    ADD CONSTRAINT fk_usebad_on_badge FOREIGN KEY (badge_id) REFERENCES badges (id);

ALTER TABLE users_badges
    ADD CONSTRAINT fk_usebad_on_user FOREIGN KEY (user_id) REFERENCES users (id);