DELIMITER //

CREATE PROCEDURE AppointmentReportGet()
BEGIN
    CREATE TEMPORARY TABLE tempReport
    SELECT
        MONTH(Start) AS Month,
        Type,
        COUNT(*) AS Count
    FROM
        appointments
    GROUP BY
        Month,
        Type;

    SELECT
        CASE WHEN Month = 1 THEN 'January'
            WHEN Month = 2 THEN 'February'
            WHEN Month = 3 THEN 'March'
            WHEN Month = 4 THEN 'April'
            WHEN Month = 5 THEN 'May'
            WHEN Month = 6 THEN 'June'
            WHEN Month = 7 THEN 'July'
            WHEN Month = 8 THEN 'August'
            WHEN Month = 9 THEN 'September'
            WHEN Month = 10 THEN 'October'
            WHEN Month = 11 THEN 'November'
            WHEN Month = 12 THEN 'December'
        END AS Month,
        Type,
        Count
    FROM
       tempReport;

    DROP TABLE tempReport;
END //

DELIMITER ;