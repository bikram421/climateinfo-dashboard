CREATE TABLE climate_data (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    location VARCHAR(50) NOT NULL,
    temp REAL NOT NULL CHECK(temp >= -100 and temp <= 100),
    wind REAL NOT NULL CHECK(wind >= 0 and wind < 200)
);

INSERT INTO climate_data (date, location, temp, wind) 
VALUES 
    ('2024-11-01', 'Victoria', 15.5, 12.3),
    ('2024-11-02', 'Duncan', 20.3, 8.7),
    ('2024-11-03', 'Nanaimo', 10.2, 15.0),
    ('2024-11-04', 'Duncan', 25.6, 10.4),
    ('2024-11-05', 'Tofino', 30.1, 5.2);