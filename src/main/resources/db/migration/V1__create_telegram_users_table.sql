CREATE TABLE IF NOT EXISTS telegram_users
(
    chat_id             BIGINT      NOT NULL PRIMARY KEY,
    telegram_username   TEXT        NOT NULL,
    telegram_first_name TEXT        NOT NULL,
    telegram_last_name  TEXT,
    full_name           TEXT,
    phone_number        TEXT,
    created_at          timestamptz NOT NULL,
    context             json,
    role                TEXT        NOT NULL
);
