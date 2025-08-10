\c db2markdown db2markdownuser

INSERT INTO customers (name, email) VALUES
    ('Alice Wang', 'alice@example.com'),
    ('Bob Chen', 'bob@example.com'),
    ('Carol Lin', 'carol@example.com');


INSERT INTO products (name, price, stock) VALUES
    ('Laptop A', 29999.00, 10),
    ('Mouse B', 799.00, 50),
    ('Keyboard C', 1599.00, 20);

INSERT INTO orders (customer_id, product_id, quantity) VALUES
    (1, 1, 1),  -- Alice 購買 1 台 Laptop A
    (2, 2, 2),  -- Bob 購買 2 個 Mouse B
    (3, 3, 1),  -- Carol 購買 1 個 Keyboard C
    (1, 3, 2);  -- Alice 再買 2 個 Keyboard C
