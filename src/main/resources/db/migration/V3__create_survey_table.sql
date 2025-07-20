CREATE TABLE IF NOT EXISTS survey
(
    id               bigserial primary key,
    chat_id          bigint      not null references telegram_users (chat_id),
    fio              text,
    company_turnover text,
    interests        text,
    activity_scope   text,
    created_at       timestamptz not null,
    delivered_at     timestamptz,
    status           text        not null
);
