ALTER TABLE telegram_users
    ADD partner_account_telegram_chat_id BIGINT NULL;

ALTER TABLE telegram_users
    ADD CONSTRAINT FK_TELEGRAM_USERS_ON_PARTNER_ACCOUNT_TELEGRAM_CHAT FOREIGN KEY (partner_account_telegram_chat_id) REFERENCES telegram_users (chat_id);