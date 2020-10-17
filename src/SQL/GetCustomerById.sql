DELIMITER //

CREATE PROCEDURE GetCustomerById(
    IN CustomerId INT
)
BEGIN
    SELECT
        Customer_Name,
        Address,
        Postal_Code,
        Phone,
        Create_Date,
        Created_By,
        Last_Update,
        Last_Updated_By,
        Division_ID
    FROM
        customers
    WHERE
        Customer_ID = CustomerId;
END //

DELIMITER ;