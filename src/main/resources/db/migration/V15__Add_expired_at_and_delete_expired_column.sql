ALTER TABLE tokens
    ADD expired_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE tokens
    ADD token_value VARCHAR(255);

ALTER TABLE tokens
    ADD CONSTRAINT uc_tokens_token_value UNIQUE (token_value);

ALTER TABLE tokens
    DROP COLUMN expired;

ALTER TABLE tokens
    DROP COLUMN token;