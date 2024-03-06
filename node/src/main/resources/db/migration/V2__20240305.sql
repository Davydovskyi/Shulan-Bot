DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id               BIGSERIAL    NOT NULL PRIMARY KEY,
    telegram_user_id BIGINT       NOT NULL UNIQUE,
    first_login_date TIMESTAMP    NOT NULL,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255),
    username         VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    is_active        BOOLEAN      NOT NULL,
    user_state       VARCHAR(255) NOT NULL
);