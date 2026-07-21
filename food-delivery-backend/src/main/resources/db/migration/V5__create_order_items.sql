CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             quantity SMALLINT NOT NULL
                                 CHECK(quantity > 0),
                             price  DECIMAL(10,2) NOT NULL
                                 CHECK (price >= 0),

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id)
                                     ON DELETE RESTRICT,

                             CONSTRAINT fk_order_items_product
                                 FOREIGN KEY (product_id)
                                     REFERENCES products(id)
                                     ON DELETE RESTRICT
);

CREATE INDEX idx_order_items_order
    ON order_items(order_id);

CREATE INDEX idx_order_items_product
    ON order_items(product_id);