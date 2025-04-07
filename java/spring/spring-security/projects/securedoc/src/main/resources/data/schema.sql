-- spring starts up it will run schema.sql and data.sql

BEGIN;
CREATE TABLE IF NOT EXISTS test (
    id SERIAL PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL
);

END;