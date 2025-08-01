CREATE TABLE IF NOT EXISTS telegram_users
(
    chat_id             BIGINT      NOT NULL PRIMARY KEY,
    telegram_username   TEXT        NOT NULL,
    telegram_first_name TEXT        NOT NULL,
    telegram_last_name  TEXT,
    full_name           TEXT,
    social_media        TEXT,
    phone_number        TEXT,
    created_at          timestamptz NOT NULL,
    context             json,
    role                TEXT        NOT NULL,
    status              TEXT        NOT NULL,
    banned_at           timestamptz,
    qr_code             TEXT,
    external_id         TEXT,
    info                jsonb
);
