ALTER TABLE users ADD CONSTRAINT uc_email UNIQUE (email);
CREATE UNIQUE INDEX idx_email ON users(email);