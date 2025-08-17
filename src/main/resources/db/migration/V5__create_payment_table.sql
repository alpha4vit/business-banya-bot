CREATE TABLE IF NOT EXISTS payment
(
    id          uuid primary key,
    external_id text,
    content     text        not null,
    type        text        not null,
    status      text        not null,
    amount      int         not null,
    currency    text        not null,
    chat_id     bigint      not null,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now(),
    expired_at  timestamptz
);

