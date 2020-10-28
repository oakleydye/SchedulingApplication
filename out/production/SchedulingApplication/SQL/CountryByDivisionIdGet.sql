DELIMITER //

CREATE PROCEDURE CountryByDivisionIdGet(
    IN DivisionId INT
)
BEGIN
   SELECT
        c.Country
   FROM
        countries AS c
        JOIN first_level_divisions AS d ON d.COUNTRY_ID = c.Country_ID
            AND d.Division_ID = DivisionId;
END //

DELIMITER ;