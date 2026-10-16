ALTER TABLE orders
    ADD COLUMN order_type VARCHAR(20) NOT NULL,
    ADD COLUMN delivery_address TEXT,
    ADD COLUMN delivery_latitude DECIMAL(9, 6),
    ADD COLUMN delivery_longitude DECIMAL(9, 6);

ALTER TABLE orders
    ADD CONSTRAINT chk_orders_order_type
        CHECK (order_type IN ('DELIVERY', 'PICKUP'));

ALTER TABLE orders
    ADD CONSTRAINT chk_orders_delivery_data
        CHECK (
            (
                order_type = 'DELIVERY'
                    AND delivery_address IS NOT NULL
                    AND delivery_latitude IS NOT NULL
                    AND delivery_longitude IS NOT NULL
                )
                OR
            (
                order_type = 'PICKUP'
                    AND delivery_address IS NULL
                    AND delivery_latitude IS NULL
                    AND delivery_longitude IS NULL
                )
            );