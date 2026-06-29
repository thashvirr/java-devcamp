-- Replace legacy bank products with updated catalogue and eligibility mappings.
DELETE FROM public.products;

INSERT INTO public.products (name, description, price, fulfilment_type_id, active)
VALUES
    (
        'Gold Cheque Account',
        'Cheque Account for People with a little bit of money',
        99.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Platinum Cheque Account',
        'Cheque Account for People with a little bit of money',
        199.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Signet Cheque Account',
        'Cheque Account for People with a little bit of money',
        499.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'B'),
        TRUE
    ),
    (
        'Islamic Cheque Account',
        'Cheque Account for Islamic Banking Customers',
        99.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Savings Account',
        'Savings Account for Individuals',
        0.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'SME Checking Account',
        'Cheque Account for Small Businesses',
        149.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Medium Enterprise Checking Account',
        'Cheque Account for Medium Businesses',
        299.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Large Enterprise Checking Account',
        'Cheque Account for Large Businesses',
        499.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'B'),
        TRUE
    );

-- INDIVIDUAL (1): personal cheque and savings products
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 1
FROM public.products p
WHERE p.name IN (
    'Gold Cheque Account',
    'Platinum Cheque Account',
    'Signet Cheque Account',
    'Islamic Cheque Account',
    'Savings Account'
);

-- SOLE PROP (2): small business cheque account
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 2
FROM public.products p
WHERE p.name = 'SME Checking Account';

-- NON-PROFIT (3): individual savings product
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 3
FROM public.products p
WHERE p.name = 'Savings Account';

-- CIPC (4): medium and large enterprise cheque accounts
INSERT INTO public.product_customer_types (product_id, customer_types_id)
SELECT p.id, 4
FROM public.products p
WHERE p.name IN (
    'Medium Enterprise Checking Account',
    'Large Enterprise Checking Account'
);
