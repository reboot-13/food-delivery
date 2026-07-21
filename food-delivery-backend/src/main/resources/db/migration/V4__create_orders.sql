CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        status VARCHAR(30) NOT NULL CHECK(
                            status IN (
                                       'CREATED',
                                       'COOKING',
                                       'READY',
                                       'DELIVERING',
                                       'COMPLETED',
                                       'CANCELLED')),
                        total_price DECIMAL(10,2) NOT NULL CHECK(total_price >= 0),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_orders_user
                            FOREIGN KEY(user_id)
                                REFERENCES users(id)
                                ON DELETE RESTRICT
)
