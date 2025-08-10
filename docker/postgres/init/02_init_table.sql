\c db2markdown db2markdownuser

-- 建立 Sequence
CREATE SEQUENCE seq_customer_id START 1;

-- 顧客表：customers
CREATE TABLE customers (
                           customer_id INTEGER PRIMARY KEY DEFAULT nextval('seq_customer_id'),
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) UNIQUE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE customers IS '顧客資料表';
COMMENT ON COLUMN customers.customer_id IS '顧客 ID（主鍵）';
COMMENT ON COLUMN customers.name IS '顧客名稱';
COMMENT ON COLUMN customers.email IS '顧客電子郵件';
COMMENT ON COLUMN customers.created_at IS '建立時間';

-- 建立 Sequence
CREATE SEQUENCE seq_product_id START 1;

-- 商品表：products
CREATE TABLE products (
                          product_id INTEGER PRIMARY KEY DEFAULT nextval('seq_product_id'),
                          name VARCHAR(100) NOT NULL,
                          price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
                          stock INT DEFAULT 0
);

COMMENT ON TABLE products IS '商品資料表';
COMMENT ON COLUMN products.product_id IS '商品 ID（主鍵）';
COMMENT ON COLUMN products.name IS '商品名稱';
COMMENT ON COLUMN products.price IS '價格';
COMMENT ON COLUMN products.stock IS '庫存數量';

-- 建立 Sequence
CREATE SEQUENCE seq_order_id START 1;

-- 訂單表：orders
CREATE TABLE orders (
                        order_id INTEGER PRIMARY KEY DEFAULT nextval('seq_order_id'),
                        customer_id INT NOT NULL,
                        product_id INT NOT NULL,
                        quantity INT NOT NULL CHECK (quantity > 0),
                        order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
                        CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

COMMENT ON TABLE orders IS '訂單資料表';
COMMENT ON COLUMN orders.order_id IS '訂單 ID（主鍵）';
COMMENT ON COLUMN orders.customer_id IS '顧客 ID（外鍵）';
COMMENT ON COLUMN orders.product_id IS '商品 ID（外鍵）';
COMMENT ON COLUMN orders.quantity IS '訂購數量';
COMMENT ON COLUMN orders.order_time IS '訂單時間';
