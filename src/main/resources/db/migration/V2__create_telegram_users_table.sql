CREATE TABLE IF NOT EXISTS telegram_user
(
    chat_id             BIGINT      NOT NULL PRIMARY KEY,
    telegram_username   TEXT        NOT NULL,
    telegram_first_name TEXT        NOT NULL,
    telegram_last_name  TEXT,
    full_name           TEXT,
    social_media        TEXT,
    phone_number        TEXT,
    created_at          timestamptz NOT NULL,
    context             jsonb,
    role                TEXT        NOT NULL,
    status              TEXT        NOT NULL,
    banned_at           timestamptz,
    resident_until      timestamptz,
    qr_code             TEXT,
    external_id         TEXT,
    user_info_id        bigint references user_info(internal_id)
);

create index tg_user_external_id_index on telegram_user(external_id);
