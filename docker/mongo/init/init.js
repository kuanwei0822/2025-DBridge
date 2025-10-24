// 連到 "testdb"（來自 MONGO_INITDB_DATABASE 環境變數）
const dbName = 'testdb';
const db = db.getSiblingDB(dbName);

// 清空（如果重建用）
db.customers.drop();
db.products.drop();
db.orders.drop();

// 建立 customers 集合並插入資料
db.customers.insertMany([
    // 3 筆簡單資料
    { _id: 1, name: "Alice Wang", email: "alice@example.com", created_at: new Date("2025-08-10T05:51:38.691Z") },
    { _id: 2, name: "Bob Chen", email: "bob@example.com", created_at: new Date("2025-08-10T05:51:38.691Z") },
    { _id: 3, name: "Carol Lin", email: "carol@example.com", created_at: new Date("2025-08-10T05:51:38.691Z") },

    // 5 筆複雜結構資料
    {
        _id: 4,
        name: "David Liu",
        email: "david.liu@corp.com",
        status: "Active",
        profile: {
            role: "Manager",
            department: "Sales",
            address: { // 第三層
                city: "Taipei",
                zip: "100"
            }
        },
        tags: ["VIP", "Corporate"],
        last_login: new Date()
    },
    {
        _id: 5,
        name: "Eva Huang",
        email: "eva.h@design.com",
        status: "Inactive",
        profile: {
            role: "Designer",
            department: "Creative"
        },
        preferences: {
            theme: "dark",
            notifications: { // 第三層
                email: false,
                sms: true
            }
        },
        tags: ["Freelancer"],
        last_login: new Date(new Date().getTime() - 86400000 * 30) // 30天前
    },
    {
        _id: 6,
        name: "Frank Wu",
        email: "frank@outlook.com",
        status: "Active",
        profile: {
            role: "Engineer",
            department: "R&D",
            salary: 75000
        },
        tags: ["Tech", "Beta Tester"],
        orders: [ // 陣列結構
            { id: "A101", amount: 500, date: new Date() },
            { id: "B202", amount: 1200, date: new Date() }
        ]
    },
    {
        _id: 7,
        name: "Grace Hsu",
        email: "grace@mail.com",
        status: "Active",
        profile: {
            role: "Marketing",
            department: "Marketing",
            address: {
                city: "Kaohsiung"
            }
        },
        tags: ["New", "Premium"],
        meta: {
            source: "Web",
            campaign: "Summer_2025"
        }
    },
    {
        _id: 8,
        name: "Henry Kuo",
        email: "henry@email.net",
        status: "Active",
        profile: {
            role: "Support",
            department: "Service"
        },
        tags: [], // 空陣列
        last_login: new Date()
    }
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
