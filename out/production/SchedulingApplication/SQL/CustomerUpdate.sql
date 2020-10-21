DELIMITER //

CREATE PROCEDURE CustomerUpdate(
    IN CustomerId INT(10),
    IN Name VARCHAR(50),
    IN Address1 VARCHAR(100),
    IN Zip VARCHAR(50),
    IN PhoneNum VARCHAR(50),
    IN CurrentUser VARCHAR(50),
    IN DivisionId INT(10)
)
BEGIN
    UPDATE
        customers
    SET
        Customer_Name = Name,
        Address = Address1,
        Postal_Code = Zip,
        Phone = PhoneNum,
        Division_ID = DivisionId,
        Last_Update = CURRENT_TIMESTAMP(),
        Last_Updated_By = CurrentUser
    WHERE
        Customer_ID = CustomerId;
END //

DELIMITER ;