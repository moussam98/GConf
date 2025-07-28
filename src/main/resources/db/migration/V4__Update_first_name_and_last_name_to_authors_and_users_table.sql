ALTER TABLE authors
    ADD first_name VARCHAR(255);

ALTER TABLE authors
    ADD last_name VARCHAR(255);

ALTER TABLE users
    ADD first_name VARCHAR(255);

ALTER TABLE users
    ADD last_name VARCHAR(255);

ALTER TABLE authors
DROP
COLUMN firstname;

ALTER TABLE authors
DROP
COLUMN lastname;

ALTER TABLE users
DROP
COLUMN firstname;

ALTER TABLE users
DROP
COLUMN lastname;