\c db2markdown db2markdownuser

-- Customers
INSERT INTO second_schema.customers (customer_id, name, email)
VALUES
    (1, 'Alice', 'alice@example.com'),
    (2, 'Bob',   'bob@example.com');

-- Products
INSERT INTO second_schema.products (product_id, name, price, stock)
VALUES
    (1, 'USB-C Cable',         199.00, 100),
    (2, 'Mechanical Keyboard', 2990.00, 30),
    (3, 'PostgreSQL Handbook', 850.00,  50);

-- Orders
INSERT INTO second_schema.orders (order_id, customer_id, status)
VALUES
    (1, 1, 'NEW');  -- Alice 的訂單

-- Order Items（價格手動對齊上面 products 的 price）
INSERT INTO second_schema.order_items (order_id, product_id, quantity, unit_price)
VALUES
    (1, 2, 1, 2990.00),  -- Mechanical Keyboard * 1
    (1, 1, 2,  199.00);  -- USB-C Cable * 2

-- Payments（部分付款 1500）
INSERT INTO second_schema.payments (payment_id, order_id, amount, method)
VALUES
    (1, 1, 1500.00, 'CARD');
