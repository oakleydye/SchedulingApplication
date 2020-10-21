DELIMITER //

CREATE PROCEDURE CountriesGet()
BEGIN
    SELECT
        Country
    FROM
        countries;
END //

DELIMITER ;