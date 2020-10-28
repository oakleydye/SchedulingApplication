DELIMITER //

CREATE PROCEDURE DivisionByDivisionIdGet(
    IN DivisionId INT
)
BEGIN
   SELECT
        Division
    FROM
         first_level_divisions
    WHERE
          Division_ID = DivisionId;
END //

DELIMITER ;