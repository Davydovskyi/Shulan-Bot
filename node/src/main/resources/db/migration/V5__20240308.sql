DROP TABLE IF EXISTS app_photo;

CREATE TABLE IF NOT EXISTS app_photo
(
    id                BIGSERIAL PRIMARY KEY,
    telegram_file_id  VARCHAR(255) NOT NULL UNIQUE,
    binary_content_id BIGINT       NOT NULL REFERENCES binary_content (id),
    file_size         INTEGER       NOT NULL
);