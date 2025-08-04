CREATE TABLE IF NOT EXISTS event
(
    id                  uuid primary key,
    external_id         text,
    title               text not null,
    speaker             text,
    carry_date          text,
    full_description    text,
    preview_description text,
    photo               text,
    registration_link   text,
    table_link          text
);

create index event_external_id_index on event(external_id);

