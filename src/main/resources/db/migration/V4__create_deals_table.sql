CREATE TABLE deals
(
    id                    VARCHAR(255)  NOT NULL,
    flow                  VARCHAR(255)  NULL,
    status                VARCHAR(255)  NULL,
    telegram_user_chat_id BIGINT        NULL,
    source                VARCHAR(255)  NULL,
    source2               VARCHAR(255)  NULL,
    deal_type             VARCHAR(255)  NULL,
    comment               VARCHAR(2000) NULL,
    contact_phone         VARCHAR(255)  NULL,
    contact_name          VARCHAR(255)  NULL,
    requested_phone       VARCHAR(255)  NULL,
    address               VARCHAR(255)  NULL,
    city                  VARCHAR(255)  NULL,
    region                VARCHAR(255)  NULL,
    customer_name         VARCHAR(255)  NULL,
    deceased_surname      VARCHAR(255)  NULL,
    CONSTRAINT pk_deals PRIMARY KEY (id)
);

ALTER TABLE deals
    ADD CONSTRAINT FK_DEALS_ON_TELEGRAM_USER_CHAT FOREIGN KEY (telegram_user_chat_id) REFERENCES telegram_users (chat_id);