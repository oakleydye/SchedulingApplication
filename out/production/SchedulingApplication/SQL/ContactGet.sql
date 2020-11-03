DELIMITER //

CREATE PROCEDURE ContactGet()
BEGIN
    SELECT
        Contact_ID,
        Contact_Name,
        Email
    FROM
         contacts;
END //

DELIMITER ;