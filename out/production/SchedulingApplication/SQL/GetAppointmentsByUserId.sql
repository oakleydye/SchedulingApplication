DELIMITER //

CREATE PROCEDURE GetAppointmentsByUser(
    IN UserId INT,
    IN Flag TINYINT
)
BEGIN
    SET Flag = IFNULL(Flag, 0);

    IF Flag = 0 THEN /*0 = All*/
        BEGIN
            SELECT
                a.Appointment_ID,
                a.Title,
                a.Description,
                a.Location,
                c.Contact_ID,
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
                a.User_ID = UserId
            ORDER BY
                a.Start;
        END;
    ELSEIF Flag = 1 THEN /*1 = Month*/
        BEGIN
            SELECT
                a.Appointment_ID,
                a.Title,
                a.Description,
                a.Location,
                c.Contact_ID,
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
                a.User_ID = UserId
                AND YEAR(a.Start) = YEAR(NOW())
                AND MONTH(a.Start) = MONTH(NOW())
            ORDER BY
                a.Start;
        END;
    ELSEIF Flag = 2 THEN /*2 = Week*/
        BEGIN
            SELECT
                a.Appointment_ID,
                a.Title,
                a.Description,
                a.Location,
                c.Contact_ID,
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
                a.User_ID = UserId
                AND YEAR(a.Start) = YEAR(NOW())
                AND MONTH(a.Start) = MONTH(NOW())
                AND WEEK(a.Start) = WEEK(NOW())
            ORDER BY
                a.Start;
        END;
    END IF;
END //

DELIMITER ;