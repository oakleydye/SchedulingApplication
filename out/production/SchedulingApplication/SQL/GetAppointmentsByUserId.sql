DELIMITER //

CREATE PROCEDURE GetAppointmentsByUser(
    IN UserId INT
)
BEGIN
    SELECT
        a.Appointment_ID,
        a.Title,
        a.Description,
        a.Location,
        c.Contact_Name,
        c.Email,
        a.Type,
        a.Start,
        a.End,
        a.Customer_ID
    FROM
        appointments AS a
        JOIN contacts AS c ON c.Contact_ID = a.Contact_ID
    WHERE
        a.User_ID = UserId;
END //

DELIMITER ;