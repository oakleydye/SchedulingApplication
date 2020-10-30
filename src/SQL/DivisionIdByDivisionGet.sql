DELIMITER //

CREATE PROCEDURE DivisionIdByDivisionGet(
    IN DivisionName VARCHAR(50)
)
BEGIN
    SELECT
        Division_ID
    FROM
        first_level_divisions
    WHERE
          Division = DivisionName;
END //

DELIMITER ;