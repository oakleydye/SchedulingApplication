DELIMITER //

CREATE PROCEDURE CustomerInfoGet()
BEGIN
    SELECT
        Customer_ID,
        Customer_Name,
        Address,
        Postal_Code,
        Phone,
        Division_ID
    FROM
        customers;
END //

DELIMITER ;