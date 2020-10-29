DELIMITER //

CREATE PROCEDURE ContactsGet()
BEGIN
    SELECT
        Contact_ID,
        Contact_Name,
        Email
    FROM
         contacts;
END //

DELIMITER ;