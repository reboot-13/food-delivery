ALTER TABLE order_items
    RENAME COLUMN price TO price_at_purchase;

ALTER TABLE order_items
    ADD COLUMN product_name VARCHAR(255) NOT NULL default '';

ALTER TABLE order_items
    ADD COLUMN product_image_url TEXT;