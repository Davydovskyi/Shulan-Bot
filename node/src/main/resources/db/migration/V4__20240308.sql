DROP TABLE IF EXISTS app_document;

CREATE TABLE IF NOT EXISTS app_document
(
    id                BIGSERIAL PRIMARY KEY,
    telegram_file_id  VARCHAR(255) NOT NULL UNIQUE,
    doc_name          VARCHAR(255) NOT NULL,
    binary_content_id BIGINT       NOT NULL REFERENCES binary_content (id),
    mime_type         VARCHAR(255) NOT NULL,
    file_size         BIGINT       NOT NULL
);