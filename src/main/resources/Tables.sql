DROP TABLE Product IF EXISTS;
DROP TABLE Seller IF EXISTS;
CREATE TABLE Seller (
    seller_id int primary key,
    name varchar(255) not null
);
CREATE TABLE Product (
    product_id int primary key,
    name varchar(255) not null,
    price int,
    seller int references Seller(seller_id)
);
--INSERT INTO Seller (seller_id, name)
--VALUES
--(1, 'Tommy Wiseau'),
--(2, 'Mark');