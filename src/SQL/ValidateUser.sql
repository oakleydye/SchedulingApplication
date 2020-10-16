DELIMITER //

CREATE PROCEDURE ValidateUser(
    IN Username VARCHAR(50),
    IN pwd VARCHAR(50)
)
BEGIN
    SELECT
        CASE WHEN Password = pwd THEN 'True'
            ELSE 'false'
        END AS Valid
    FROM
        users
    WHERE
        User_Name = Username;
END//

DELIMITER ;