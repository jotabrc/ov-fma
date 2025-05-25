CREATE TABLE IF NOT EXISTS tb_user_finance (
    id BIGSERIAL PRIMARY KEY,
    user_uuid VARCHAR(36) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT
);

CREATE TABLE IF NOT EXISTS tb_financial_entity (
    id BIGSERIAL PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    user_finance_id BIGINT NOT NULL,
    due_date DATE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    FOREIGN KEY (user_finance_id) REFERENCES tb_user_finance (id)
);

CREATE TABLE IF NOT EXISTS tb_payment (
    id BIGINT PRIMARY KEY,
    payee VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_receipt (
    id BIGINT PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_recurring_payment (
    id BIGINT PRIMARY KEY,
    payee VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_recurring_receipt (
    id BIGINT PRIMARY KEY,
    vendor VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE TABLE IF NOT EXISTS tb_recurrence (
    id BIGINT PRIMARY KEY,
    recurring_until DATE NOT NULL,
    FOREIGN KEY (id) REFERENCES tb_financial_entity (id)
);

CREATE INDEX idx_recurring_until ON tb_recurrence(recurring_until);
CREATE INDEX idx_user_finance_id ON tb_financial_entity(user_finance_id);
CREATE INDEX idx_due_date ON tb_financial_entity(due_date);
