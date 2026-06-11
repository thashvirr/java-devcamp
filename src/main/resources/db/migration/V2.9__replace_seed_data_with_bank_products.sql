-- Replace generic e-commerce seed data with bank-relevant products and fulfilment types.
DELETE FROM public.products;

DELETE FROM public.fulfilment_types;

INSERT INTO public.fulfilment_types (code, description)
VALUES
    ('D', 'Digital activation via online banking'),
    ('B', 'Branch collection of card or signed documents'),
    ('M', 'Mail delivery of card and account documents');

INSERT INTO public.products (name, description, price, fulfilment_type_id, active)
VALUES
    (
        'Savings Account',
        'Everyday savings account with no monthly service fee',
        0.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Cheque Account',
        'Transaction account with internet and mobile banking',
        99.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Student Account',
        'No-fee account for full-time students under 25',
        0.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Credit Card',
        'Classic credit card with rewards programme (annual fee)',
        450.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'M'),
        TRUE
    ),
    (
        'Home Loan',
        'Variable-rate home loan for primary residence',
        0.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Fixed Deposit',
        '12-month fixed deposit with minimum opening balance of R1 000',
        1000.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'D'),
        TRUE
    ),
    (
        'Private Banking Account',
        'Premium banking with a dedicated relationship manager (monthly service fee)',
        500.00,
        (SELECT id FROM public.fulfilment_types WHERE code = 'B'),
        TRUE
    );
