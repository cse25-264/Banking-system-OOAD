-- BANKING SYSTEM DATABASE SCHEMA (SQLite)
-- One customer has many accounts. One account has many transactions.

CREATE TABLE IF NOT EXISTS customer (
    customer_id INTEGER PRIMARY KEY,
    first_name  TEXT NOT NULL,
    surname     TEXT NOT NULL,
    address     TEXT NOT NULL,
    username    TEXT NOT NULL UNIQUE,
    password    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS account (
    account_number  TEXT PRIMARY KEY,
    customer_id     INTEGER NOT NULL,
    account_type    TEXT NOT NULL CHECK (account_type IN ('Savings', 'Investment', 'Cheque')),
    branch          TEXT NOT NULL,
    balance         REAL NOT NULL DEFAULT 0,
    company_name    TEXT,
    company_address TEXT,
    overdraft_limit REAL DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id TEXT PRIMARY KEY,
    account_number TEXT NOT NULL,
    amount         REAL NOT NULL,
    type           TEXT NOT NULL,
    tx_date        INTEGER NOT NULL,
    FOREIGN KEY (account_number) REFERENCES account (account_number) ON DELETE CASCADE
);
