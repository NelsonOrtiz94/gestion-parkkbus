-- V1__initial_schema.sql
-- Flyway initial migration — ParkKbus Suite

CREATE TABLE IF NOT EXISTS users (
    id            UUID PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    roles         VARCHAR(255) NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS spots (
    id        UUID PRIMARY KEY,
    code      VARCHAR(20)  NOT NULL UNIQUE,
    available BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS tariffs (
    id           UUID         PRIMARY KEY,
    vehicle_type VARCHAR(50)  NOT NULL UNIQUE,
    hourly_rate  NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
    id   UUID        PRIMARY KEY,
    plate VARCHAR(20) NOT NULL UNIQUE,
    type  VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS parking_sessions (
    id           UUID          PRIMARY KEY,
    vehicle_id   UUID          NOT NULL REFERENCES vehicles(id),
    spot_id      UUID          NOT NULL REFERENCES spots(id),
    entry_time   TIMESTAMP     NOT NULL,
    exit_time    TIMESTAMP,
    total_amount NUMERIC(10,2),
    status       VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS payments (
    id           UUID          PRIMARY KEY,
    session_id   UUID          NOT NULL REFERENCES parking_sessions(id),
    amount       NUMERIC(10,2) NOT NULL,
    method       VARCHAR(50)   NOT NULL,
    payment_date TIMESTAMP     NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'COMPLETED'
);

-- Seed: default tariffs
INSERT INTO tariffs (id, vehicle_type, hourly_rate)
VALUES
    (gen_random_uuid(), 'CAR',        3000.00),
    (gen_random_uuid(), 'MOTORCYCLE', 1500.00),
    (gen_random_uuid(), 'TRUCK',      5000.00)
ON CONFLICT DO NOTHING;

-- Seed: initial spots (10 spaces)
INSERT INTO spots (id, code, available)
SELECT gen_random_uuid(), 'A-' || LPAD(gs::TEXT, 2, '0'), TRUE
FROM generate_series(1, 10) gs
ON CONFLICT DO NOTHING;

