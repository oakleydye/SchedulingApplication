DELIMITER //

CREATE PROCEDURE CustomerDelete(
    IN CustomerId INT
)
BEGIN
    DELETE
    FROM
         appointments
    WHERE
        Customer_ID = CustomerId;

    DELETE
    FROM
        customers
    WHERE
        Customer_ID = CustomerId;
END //

DELIMITER ;