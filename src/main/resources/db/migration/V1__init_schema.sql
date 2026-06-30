CREATE TABLE users (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone        VARCHAR(15),
    role         VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    created_at   TIMESTAMP DEFAULT NOW()
);

CREATE TABLE products (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    description  TEXT,
    price        NUMERIC(10,2) NOT NULL,
    sale_price   NUMERIC(10,2),
    sale_ends_at TIMESTAMP,
    stock_qty    INT NOT NULL DEFAULT 0,
    category     VARCHAR(50),
    image_urls   TEXT[],
    is_active    BOOLEAN DEFAULT TRUE,
    avg_rating   NUMERIC(3,2) DEFAULT 0,
    created_at   TIMESTAMP DEFAULT NOW()
);

CREATE TABLE coupons (
    id             BIGSERIAL PRIMARY KEY,
    code           VARCHAR(50) NOT NULL UNIQUE,
    discount_type  VARCHAR(20) NOT NULL,
    discount_value NUMERIC(10,2) NOT NULL,
    min_order      NUMERIC(10,2) DEFAULT 0,
    max_uses       INT DEFAULT 100,
    used_count     INT DEFAULT 0,
    expiry         TIMESTAMP,
    is_active      BOOLEAN DEFAULT TRUE
);

CREATE TABLE carts (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity   INT NOT NULL DEFAULT 1,
    UNIQUE(user_id, product_id)
);

CREATE TABLE orders (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL REFERENCES users(id),
    full_name        VARCHAR(100),
    phone            VARCHAR(15),
    street           VARCHAR(255),
    city             VARCHAR(100),
    state            VARCHAR(100),
    pincode          VARCHAR(10),
    coupon_code      VARCHAR(50),
    subtotal         NUMERIC(10,2),
    discount_amount  NUMERIC(10,2) DEFAULT 0,
    delivery_charge  NUMERIC(10,2) DEFAULT 0,
    total_amount     NUMERIC(10,2) NOT NULL,
    payment_id       VARCHAR(100),
    razorpay_order_id VARCHAR(100),
    payment_status   VARCHAR(20) DEFAULT 'PENDING',
    order_status     VARCHAR(30) DEFAULT 'PENDING',
    created_at       TIMESTAMP DEFAULT NOW()
);

CREATE TABLE order_items (
    id                 BIGSERIAL PRIMARY KEY,
    order_id           BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id         BIGINT REFERENCES products(id),
    product_name       VARCHAR(200),
    price_at_purchase  NUMERIC(10,2) NOT NULL,
    quantity           INT NOT NULL
);

CREATE TABLE reviews (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id),
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    rating     INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment    TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id, product_id)
);