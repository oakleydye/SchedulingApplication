DELIMITER //

CREATE PROCEDURE ContactSave(
    IN Id VARCHAR(10),
    IN CustName VARCHAR(50),
    IN CustEmail VARCHAR(50)
)
BEGIN
    UPDATE
        contacts
    SET
        Contact_Name = CustName,
        Email = CustEmail
    WHERE
        Contact_ID = Id;

    IF ROW_COUNT() = 0 THEN
        INSERT INTO contacts
        (
            Contact_Name,
            Email
        )
        VALUES
        (
            CustName,
            CustEmail
        );
    END IF;
END //

DELIMITER ;