DELIMITER //

CREATE PROCEDURE AppointmentOverlapCheck(
    IN UserId INT,
    IN StartTime DATETIME,
    IN EndTime DATETIME
)
BEGIN
    SELECT
        COUNT(*)
    FROM
        appointments
    WHERE
        User_ID = UserId
        AND
        (
            (
                Start <= StartTime
                AND End >= StartTime
            )
            OR
            (
                Start <= EndTime
                AND End >= EndTime
            )
        );
END //

DELIMITER ;