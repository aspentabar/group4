DROP SCHEMA IF EXISTS realestate;
CREATE SCHEMA realestate;
USE realestate;

CREATE TABLE property (
    property_id         VARCHAR(255) NULL,
    download_date       VARCHAR(255) NULL,
    council_name        VARCHAR(255) NULL,
    purchase_price      VARCHAR(255) NULL,
    address             VARCHAR(255) NULL,
    post_code           VARCHAR(255) NULL,
    property_type       VARCHAR(255) NULL,
    strata_lot_number   VARCHAR(255) NULL,
    property_name       VARCHAR(255) NULL,
    area_type           VARCHAR(255) NULL,
    contract_date       VARCHAR(255) NULL,
    settlement_date     VARCHAR(255) NULL,
    zoning              VARCHAR(255) NULL,
    nature_of_property  VARCHAR(255) NULL,
    primary_purpose     VARCHAR(255) NULL,
    legal_description   VARCHAR(255) NULL
);

SELECT COUNT(*) FROM property;



-- DROP SCHEMA IF EXISTS realestate;
-- CREATE SCHEMA realestate;
-- USE realestate;

-- CREATE TABLE property (
-- property_id INT,
-- download_date INT,
-- council_name VARCHAR(255),
-- purchase_price INT,
-- address VARCHAR(255),
-- post_code INT,
-- property_type VARCHAR(255),
-- strata_lot_number INT,
-- property_name VARCHAR(255),
-- area_type VARCHAR (255),
-- contract_date INT,
-- settlement_date INT,
-- zoning VARCHAR(255),
-- nature_of_property VARCHAR(255),
-- primary_purpose VARCHAR(255),
-- legal_description VARCHAR(255)

-- );

-- LOAD DATA LOCAL INFILE '/nsw_property_data.csv'
-- INTO TABLE your_table_name
-- FIELDS TERMINATED BY ',' 
-- ENCLOSED BY '"' 
-- LINES TERMINATED BY '\n'
-- IGNORE 1 ROWS;

-- SELECT COUNT(*) FROM property;

