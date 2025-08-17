CREATE TABLE IF NOT EXISTS event
(
    id                  uuid primary key,
    external_id         text,
    type                text        not null,
    title               text        not null,
    speaker             text,
    carry_date          timestamptz,
    week_day            int,
    time                time,
    full_description    jsonb,
    preview_description jsonb,
    photo               jsonb,
    registration_link   text,
    table_link          text,
    created_at          timestamptz not null
);
