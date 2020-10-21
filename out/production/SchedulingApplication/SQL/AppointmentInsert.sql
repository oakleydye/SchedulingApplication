DELIMITER //

CREATE PROCEDURE AppointmentInsert(
    IN Title1 VARCHAR(50),
    IN Desc1 VARCHAR(50),
    IN Place VARCHAR(50),
    IN Type1 VARCHAR(50),
    IN StartDate DATETIME,
    IN EndDate DATETIME,
    IN CurrentUser VARCHAR(50),
    IN CustomerId INT(10),
    IN ContactId INT(10)
)
BEGIN
    INSERT INTO appointments
    (
        Title,
        Description,
        Location,
        Type,
        Start,
        End,
        Create_Date,
        Created_By,
        Last_Update,
        Last_Updated_By,
        Customer_ID,
        User_ID,
        Contact_ID
    )
    SELECT
        Title1,
        Desc1,
        Place,
        Type1,
        StartDate,
        EndDate,
        CURRENT_TIMESTAMP(),
        CurrentUser,
        CURRENT_TIMESTAMP(),
        CurrentUser,
        CustomerId,
        u.User_ID,
        ContactId
    FROM
         users AS u
    WHERE
          u.User_Name = CurrentUser;

END //

DELIMITER ;