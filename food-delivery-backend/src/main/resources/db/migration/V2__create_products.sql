CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          price DECIMAL(10,2) NOT NULL,
                          image_url TEXT,
                          weight SMALLINT,
                          calories SMALLINT,
                          available BOOLEAN NOT NULL DEFAULT TRUE,
                          category_id BIGINT NOT NULL,

                          FOREIGN KEY (category_id)
                              REFERENCES categories(id)
);