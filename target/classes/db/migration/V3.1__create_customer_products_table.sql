-- Records products that a customer has taken up.
CREATE TABLE public.customer_products (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES public.products (id),
    taken_up_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (customer_id, product_id)
);
