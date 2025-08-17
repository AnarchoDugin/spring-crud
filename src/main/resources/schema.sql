DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    category VARCHAR(50) NOT NULL
);

INSERT INTO products (name, description, price, quantity, category) VALUES
    ('Honor X8', 'Huawei Honor X8', 150.99, 10, 'Smartphone'),
    ('iPhone 12', 'Apple iPhone 12', 450.00, 10, 'Smartphone'),
    ('HP XLS-12 SE', 'Hewlett-Packard XLS-12 SE', 120.00, 5, 'Laptop'),
    ('MacBook Air Pro M2', 'Apple MacBook Air Pro M2', 399.99, 20, 'Laptop');