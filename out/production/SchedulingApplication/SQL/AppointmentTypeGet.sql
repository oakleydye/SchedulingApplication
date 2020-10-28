DELIMITER //

CREATE PROCEDURE AppointmentTypeGet()
BEGIN
   SELECT DISTINCT
        c.Customer_Name,
        a.Type
    FROM
        customers AS c
        JOIN appointments AS a ON c.Customer_ID = a.Customer_ID;

END //

DELIMITER ;