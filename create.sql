CREATE TABLE IF NOT EXISTS public.webpoints
(
    attempt_id integer PRIMARY KEY,
    x double precision NOT NULL,
    y double precision,
    r double precision,
    inarea boolean
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.webpoints
    OWNER to postgres;