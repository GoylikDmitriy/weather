create table weather
(
    id              BIGSERIAL PRIMARY KEY,
    temperature          double precision,
    wind_speed           double precision,
    atmospheric_pressure double precision,
    humidity             double precision,
    weather_conditions   varchar,
    location             varchar,
    date_time            timestamp
);