DELIMITER //

CREATE PROCEDURE AppointmentOverlapCheck(
    IN UserId INT,
    IN StartTime DATETIME,
    IN EndTime DATETIME
)
BEGIN
    SELECT
        COUNT(*) AS OverlapCount
    FROM
        appointments
    WHERE
        User_ID = UserId
        AND
        (
            Start < EndTime
            AND End > StartTime
        );
END //

DELIMITER ;