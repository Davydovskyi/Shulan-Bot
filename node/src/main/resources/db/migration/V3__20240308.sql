DROP TABLE IF EXISTS binary_content;

CREATE TABLE IF NOT EXISTS binary_content
(
    id      BIGSERIAL PRIMARY KEY,
    content BYTEA NOT NULL
);