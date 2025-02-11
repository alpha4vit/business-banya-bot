CREATE TABLE telegram_users
(
    chat_id             BIGINT       NOT NULL,
    telegram_username   VARCHAR(255) NULL,
    telegram_first_name VARCHAR(255) NULL,
    telegram_last_name  VARCHAR(255) NULL,
    full_name           VARCHAR(255) NULL,
    created_at          datetime     NULL,
    context             JSON         NULL,
    `role`              VARCHAR(255) NULL,
    active              BIT(1)       NOT NULL,
    region_id           BIGINT       NULL,
    CONSTRAINT pk_telegram_users PRIMARY KEY (chat_id)
);

ALTER TABLE telegram_users
    ADD CONSTRAINT FK_TELEGRAM_USERS_ON_REGION FOREIGN KEY (region_id) REFERENCES regions (id);