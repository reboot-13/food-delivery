CREATE TABLE cart_items(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity SMALLINT NOT NULL,

CONSTRAINT fk_cart_items_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE,

CONSTRAINT fk_cart_items_product
    FOREIGN KEY (product_id)
    REFERENCES products(id),

CONSTRAINT uq_cart_items_user_product
    UNIQUE (user_id, product_id),

CONSTRAINT chk_cart_items_quantity
    CHECK (quantity > 0)
);