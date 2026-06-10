CREATE TABLE public.fulfilment_types (
    id BIGSERIAL PRIMARY KEY,
    code CHAR(1) NOT NULL UNIQUE,
    description TEXT
);
