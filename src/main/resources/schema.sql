-- =============================================
-- AgroVault PostgreSQL Schema
-- =============================================

-- Users
CREATE TABLE IF NOT EXISTS users (
    id         UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(30)  NOT NULL,
    created_at TIMESTAMP    DEFAULT now()
);

-- Cities
CREATE TABLE IF NOT EXISTS cities (
    id        SERIAL        PRIMARY KEY,
    name      VARCHAR(100)  NOT NULL UNIQUE,
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

-- Storages
CREATE TABLE IF NOT EXISTS storages (
    id                 UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
    name               VARCHAR(150)     NOT NULL,
    owner_id           UUID             NOT NULL REFERENCES users(id),
    city_id            INT              NOT NULL REFERENCES cities(id),
    latitude           DOUBLE PRECISION NOT NULL,
    longitude          DOUBLE PRECISION NOT NULL,
    total_capacity     DOUBLE PRECISION NOT NULL,
    available_capacity DOUBLE PRECISION NOT NULL,
    temperature_min    DOUBLE PRECISION,
    temperature_max    DOUBLE PRECISION,
    version            BIGINT           DEFAULT 0,
    created_at         TIMESTAMP        DEFAULT now()
);

-- Bookings
CREATE TABLE IF NOT EXISTS bookings (
    id           UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id    UUID             NOT NULL REFERENCES users(id),
    storage_id   UUID             NOT NULL REFERENCES storages(id),
    produce_type VARCHAR(100),
    quantity     DOUBLE PRECISION NOT NULL,
    start_date   DATE             NOT NULL,
    end_date     DATE             NOT NULL,
    status       VARCHAR(30)      NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMP        DEFAULT now()
);

-- Temperature Logs
CREATE TABLE IF NOT EXISTS temperature_logs (
    id          SERIAL           PRIMARY KEY,
    storage_id  UUID             NOT NULL REFERENCES storages(id),
    temperature DOUBLE PRECISION NOT NULL,
    humidity    DOUBLE PRECISION,
    recorded_at TIMESTAMP        DEFAULT now()
);

-- =============================================
-- Indexes
-- =============================================

CREATE INDEX IF NOT EXISTS idx_storages_city_id       ON storages(city_id);
CREATE INDEX IF NOT EXISTS idx_bookings_farmer_id     ON bookings(farmer_id);
CREATE INDEX IF NOT EXISTS idx_bookings_storage_id    ON bookings(storage_id);
CREATE INDEX IF NOT EXISTS idx_temp_logs_storage_id   ON temperature_logs(storage_id);
CREATE INDEX IF NOT EXISTS idx_temp_logs_recorded_at  ON temperature_logs(recorded_at);
