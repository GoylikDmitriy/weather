create table weather.weather
(
    weather_id           bigserial
        constraint weather_pk
            primary key,
    temperature          double precision,
    wind_speed           double precision,
    atmospheric_pressure double precision,
    humidity             double precision,
    weather_conditions   varchar,
    location             varchar,
    date_time            date
);

create unique index weather_weather_id_uindex
    on weather.weather (weather_id);