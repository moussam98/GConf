ALTER TABLE call_for_papers_topics
    DROP CONSTRAINT fk_calforpaptop_on_call_for_papers;

ALTER TABLE submissions_keywords
    DROP CONSTRAINT fk_subkey_on_submission;

ALTER TABLE submissions
    ADD keywords VARCHAR(255);

ALTER TABLE submissions
    ALTER COLUMN keywords SET NOT NULL;

ALTER TABLE call_for_papers
    ADD topics VARCHAR(255);

ALTER TABLE call_for_papers
    ALTER COLUMN topics SET NOT NULL;

DROP TABLE call_for_papers_topics CASCADE;

DROP TABLE submissions_keywords CASCADE;

DROP TABLE keywords CASCADE;

DROP TABLE topics CASCADE;

ALTER TABLE conferences
    ADD CONSTRAINT uc_conferences_name UNIQUE (name);

ALTER TABLE conferences
    ADD CONSTRAINT uc_conferences_acronym UNIQUE (acronym);