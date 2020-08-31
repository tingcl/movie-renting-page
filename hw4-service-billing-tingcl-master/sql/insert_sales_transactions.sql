DELIMITER $$
DROP PROCEDURE IF EXISTS insert_sales_transactions $$
CREATE PROCEDURE insert_sales_transactions
(IN email varchar(50),IN movieId varchar(10),
 IN quantity int, IN saleDate date, IN sId int,
 IN token varchar(50))
BEGIN
INSERT INTO sales (email, movieId, quantity, saleDate) VALUES (email, movieId, quantity, saleDate);
INSERT INTO transactions (sId, token) VALUES (LAST_INSERT_ID(), token);
END$$
DELIMITER ;