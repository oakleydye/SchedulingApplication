DELIMITER //

CREATE PROCEDURE CustomerInsert(
    IN Name VARCHAR(50),
    IN Address1 VARCHAR(100),
    IN Zip VARCHAR(50),
    IN PhoneNum VARCHAR(50),
    IN CurrentUser VARCHAR(50),
    IN DivisionId INT(10)
)
BEGIN
    INSERT INTO customers
    (
        Customer_Name,
        Address,
        Postal_Code,
        Phone,
        Create_Date,
        Created_By,
        Last_Update,
        Last_Updated_By,
        Division_ID
    )
    VALUES
    (
        Name,
        Address1,
        Zip,
        PhoneNum,
        CURRENT_TIMESTAMP(),
        CurrentUser,
        CURRENT_TIMESTAMP(),
        CurrentUser,
        DivisionId
    );
END //

DELIMITER ;