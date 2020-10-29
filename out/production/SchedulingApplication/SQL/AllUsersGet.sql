DELIMITER //

CREATE PROCEDURE AllUsersGet()
BEGIN
    SELECT
        User_Name
    FROM
         users;
END //

DELIMITER ;