ALTER TABLE conferences
    RENAME COLUMN first_day TO start_date;

ALTER TABLE conferences
    RENAME COLUMN  last_day TO end_date;

ALTER TABLE conferences
    RENAME COLUMN user_id TO owner_id;