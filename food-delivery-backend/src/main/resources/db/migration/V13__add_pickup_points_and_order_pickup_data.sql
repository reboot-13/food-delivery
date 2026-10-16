CREATE TABLE pickup_points (
                               id BIGSERIAL PRIMARY KEY,

                               title VARCHAR(100) NOT NULL,
                               address VARCHAR(255) NOT NULL,

                               latitude DECIMAL(9, 6) NOT NULL,
                               longitude DECIMAL(9, 6) NOT NULL,

                               working_hours VARCHAR(100),

                               active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_pickup_points_active
    ON pickup_points(active);


ALTER TABLE orders
    ADD COLUMN pickup_point_id BIGINT,
    ADD COLUMN pickup_point_title VARCHAR(100),
    ADD COLUMN pickup_point_address VARCHAR(255),
    ADD COLUMN pickup_point_latitude DECIMAL(9, 6),
    ADD COLUMN pickup_point_longitude DECIMAL(9, 6);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_pickup_point
        FOREIGN KEY (pickup_point_id)
            REFERENCES pickup_points(id)
            ON DELETE SET NULL;


ALTER TABLE orders
    ADD CONSTRAINT ck_orders_destination
        CHECK (
            (
                order_type = 'DELIVERY'
                    AND delivery_address IS NOT NULL
                    AND delivery_latitude IS NOT NULL
                    AND delivery_longitude IS NOT NULL

                    AND pickup_point_id IS NULL
                    AND pickup_point_title IS NULL
                    AND pickup_point_address IS NULL
                    AND pickup_point_latitude IS NULL
                    AND pickup_point_longitude IS NULL
                )
                OR
            (
                order_type = 'PICKUP'

                    AND delivery_address IS NULL
                    AND delivery_latitude IS NULL
                    AND delivery_longitude IS NULL

                    AND pickup_point_id IS NOT NULL
                    AND pickup_point_title IS NOT NULL
                    AND pickup_point_address IS NOT NULL
                    AND pickup_point_latitude IS NOT NULL
                    AND pickup_point_longitude IS NOT NULL
                )
            );