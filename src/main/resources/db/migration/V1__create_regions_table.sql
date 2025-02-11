CREATE TABLE regions
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_regions PRIMARY KEY (id)
);

CREATE UNIQUE INDEX regionNameIndex ON regions (name);