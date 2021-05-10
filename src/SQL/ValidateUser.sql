DELIMITER //

CREATE PROCEDURE ValidateUser(
    IN Username VARCHAR(50),
    IN pwd VARCHAR(50)
)
BEGIN
    SELECT
        IF(Password = pwd, 'True', 'false') AS Valid
    FROM
        users
    WHERE
        User_Name = Username;
END//

DELIMITER ;