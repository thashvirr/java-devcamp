-- Links products to the customer types that are eligible to take them up.
CREATE TABLE public.product_customer_types (
    product_id BIGINT NOT NULL REFERENCES public.products (id) ON DELETE CASCADE,
    customer_types_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, customer_types_id)
);

-- INDIVIDUAL (1): retail banking products
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 1
FROM public.products p
WHERE p.name IN (
    'Savings Account',
    'Cheque Account',
    'Student Account',
    'Credit Card',
    'Home Loan',
    'Fixed Deposit',
    'Private Banking Account'
);

-- SOLE PROP (2): business-oriented transaction and credit products
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 2
FROM public.products p
WHERE p.name IN ('Cheque Account', 'Credit Card');

-- NON-PROFIT (3): basic deposit and transaction accounts
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 3
FROM public.products p
WHERE p.name IN ('Savings Account', 'Cheque Account');

-- CIPC (4): registered company transaction account only
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 4
FROM public.products p
WHERE p.name = 'Cheque Account';
