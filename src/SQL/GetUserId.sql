DELIMITER //

CREATE PROCEDURE GetUserId(
    IN UserName VARCHAR(50)
)
BEGIN
    SELECT
        User_ID
    FROM
        users
    WHERE
        User_Name = UserName;

END //

DELIMITER ;