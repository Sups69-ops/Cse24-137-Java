-- migrations/001_create_schema.sql
-- Creates the initial schema: users, accounts, transactions

-- users table
CREATE TABLE IF NOT EXISTS users (
  customer_id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  password_hash TEXT NOT NULL,
  address TEXT,
  phone TEXT
);

-- accounts table
CREATE TABLE IF NOT EXISTS accounts (
  account_number TEXT PRIMARY KEY,
  customer_id TEXT NOT NULL,
  balance REAL NOT NULL DEFAULT 0.0,
  account_type TEXT NOT NULL,
  branch TEXT,
  FOREIGN KEY(customer_id) REFERENCES users(customer_id)
);

-- transactions table
CREATE TABLE IF NOT EXISTS transactions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  account_number TEXT NOT NULL,
  type TEXT NOT NULL,
  amount REAL NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(account_number) REFERENCES accounts(account_number)
);
