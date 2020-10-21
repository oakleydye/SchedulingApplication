DELIMITER //

CREATE PROCEDURE AppointmentUpdate(
    IN AppointmentId INT(10),
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
    UPDATE
        appointments
    SET
        Title = Title1,
        Description = Desc1,
        Location = Place,
        Type = Type1,
        Start = StartDate,
        End = EndDate,
        Last_Update = CURRENT_TIMESTAMP(),
        Last_Updated_By = CurrentUser,
        Customer_ID = CustomerId,
        User_ID = (SELECT User_ID FROM users WHERE User_Name = CurrentUser),
        Contact_ID = ContactId
    WHERE
          Appointment_ID = AppointmentId;
END //

DELIMITER ;