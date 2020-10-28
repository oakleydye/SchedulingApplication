DELIMITER //

CREATE PROCEDURE AppointmentByCustomerMonthTypeGet(
    IN CustomerName VARCHAR(50),
    IN MonthToCheck VARCHAR(50),
    IN AppointmentType VARCHAR(50)
)
BEGIN
   SELECT
        COUNT(*) AS Count
    FROM
        appointments AS a
        JOIN customers AS c ON a.Customer_ID = c.Customer_ID
            AND c.Customer_Name = CustomerName
    WHERE
        MONTH(a.Start) = MonthToCheck
        AND YEAR(a.Start) = YEAR(CURRENT_TIMESTAMP())
        AND a.Type = AppointmentType;
END //

DELIMITER ;