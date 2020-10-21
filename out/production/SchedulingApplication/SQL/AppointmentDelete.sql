DELIMITER //

CREATE PROCEDURE AppointmentDelete(
    IN AppointmentId INT(10)
)
BEGIN
    DELETE
    FROM
         appointments
    WHERE
          Appointment_ID = AppointmentId;
END //

DELIMITER ;