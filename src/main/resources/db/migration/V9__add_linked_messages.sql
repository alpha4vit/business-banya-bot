CREATE TABLE linked_messages
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    deal_id    VARCHAR(255) NULL,
    chat_id    BIGINT       NULL,
    message_id INT          NULL,
    CONSTRAINT pk_linked_messages PRIMARY KEY (id)
);

ALTER TABLE linked_messages
    ADD CONSTRAINT FK_LINKED_MESSAGES_ON_CHAT FOREIGN KEY (chat_id) REFERENCES telegram_users (chat_id);

ALTER TABLE linked_messages
    ADD CONSTRAINT FK_LINKED_MESSAGES_ON_DEAL FOREIGN KEY (deal_id) REFERENCES deals (id);