CREATE extension IF NOT EXISTS "uuid-ossp";

CREATE TYPE user_authorities AS ENUM (
    'user',
    'admin'
);

CREATE TABLE IF NOT EXISTS "user_info"
(
    id              VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    authorities     user_authorities NOT NULL,
    password        VARCHAR NOT NULL,
    email           VARCHAR UNIQUE NOT NULL
);