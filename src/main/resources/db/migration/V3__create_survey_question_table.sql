CREATE TABLE IF NOT EXISTS survey_question
(
    number        bigint primary key,
    message       text   not null,
    scenario_step text   not null unique
);
