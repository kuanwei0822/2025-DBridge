// 連到 "testdb"（來自 MONGO_INITDB_DATABASE 環境變數）
const dbName = 'testdb';
const db = db.getSiblingDB(dbName);

// 清空（如果重建用）
db.customers.drop();
db.products.drop();
db.orders.drop();

// 建立 customers 集合並插入資料
db.customers.insertMany([
    { _id: 1, name: "Alice Wang", email: "alice@example.com", created_at: new Date() },
    { _id: 2, name: "Bob Chen", email: "bob@example.com", created_at: new Date() },
    { _id: 3, name: "Carol Lin", email: "carol@example.com", created_at: new Date() }
]);

// 建立 products 集合並插入資料
db.products.insertMany([
    { _id: 1, name: "Laptop A", price: 29999.00, stock: 10 },
    { _id: 2, name: "Mouse B", price: 799.00, stock: 50 },
    { _id: 3, name: "Keyboard C", price: 1599.00, stock: 20 }
]);

// 建立 orders 集合並插入資料（引用上面 customers/products 的 _id）
db.orders.insertMany([
    { _id: 1, customer_id: 1, product_id: 1, quantity: 1, order_time: new Date() },
    { _id: 2, customer_id: 2, product_id: 2, quantity: 2, order_time: new Date() },
    { _id: 3, customer_id: 3, product_id: 3, quantity: 1, order_time: new Date() },
    { _id: 4, customer_id: 1, product_id: 3, quantity: 2, order_time: new Date() }
]);

// 建立索引（選擇性，但建議加）
db.customers.createIndex({ email: 1 }, { unique: true });
db.products.createIndex({ name: 1 });
db.orders.createIndex({ customer_id: 1 });
db.orders.createIndex({ product_id: 1 });

print(`Database '${dbName}' initialized with sample data.`);
