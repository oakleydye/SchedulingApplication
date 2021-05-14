DELIMITER //

CREATE PROCEDURE NewUserInsert(
    IN UserName VARCHAR(50),
    IN pwd VARCHAR(100),
    IN CurrentUser VARCHAR(50)
)
BEGIN
    INSERT INTO users
    (
        User_Name,
        Password,
        Created_By,
        Last_Updated_By
    )
    VALUES
    (
        UserName,
        pwd,
        CurrentUser,
        CurrentUser
    );
END //

DELIMITER ;