DROP TABLE IF EXISTS raw_data;

CREATE TABLE raw_data
(
    id    BIGSERIAL PRIMARY KEY,
    event jsonb NOT NULL
);