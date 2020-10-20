DELIMITER //

CREATE PROCEDURE DivisionByCountryGet(
    IN Country VARCHAR(50)
)
BEGIN
    SELECT
        f.Division
    FROM
        first_level_divisions AS f
        JOIN countries AS c ON c.Country_ID = f.COUNTRY_ID
            AND c.Country = Country;
END //

DELIMITER ;