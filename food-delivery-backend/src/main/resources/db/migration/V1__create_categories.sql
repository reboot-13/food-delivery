CREATE TABLE categories(
    id BIGSERIAL PRIMARY KEY,
    name varchar(100) not null UNIQUE
);

INSERT INTO categories (name) VALUES
('Пицца'),
('Бургеры'),
('Суши'),
('Вок и лапша'),
('Салаты'),
('Десерты'),
('Напитки');