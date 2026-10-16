CREATE TABLE addresses (
                           id BIGSERIAL PRIMARY KEY,

                           user_id BIGINT NOT NULL,

                           title VARCHAR(50),

                           city VARCHAR(100) NOT NULL,
                           street VARCHAR(150) NOT NULL,
                           house VARCHAR(20) NOT NULL,
                           apartment VARCHAR(20),

                           entrance VARCHAR(20),
                           floor SMALLINT,
                           intercom VARCHAR(20),

                           comment TEXT,

                           latitude DECIMAL(9, 6) NOT NULL,
                           longitude DECIMAL(9, 6) NOT NULL,

                           CONSTRAINT fk_addresses_user
                               FOREIGN KEY (user_id)
                                   REFERENCES users(id)
                                   ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id
    ON addresses(user_id);