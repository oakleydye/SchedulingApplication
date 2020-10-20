DELIMITER //

CREATE PROCEDURE DivisionGet()
BEGIN
    SELECT
        Division
    FROM
        first_level_divisions;
END //

DELIMITER ;