# weather

1. Получение текущей погоды.
  URL: /weather/current
  
  Метод: GET.
  
  Пример запроса:
  GET /weather/current
  
  Пример ответа:
  {
    "id":"1",
    "temp":"10.2",
    "wind_mph":"3.4",
    "pressure_mb":"1025.0",
    "humidity":"73.5",
    "weather_conditions":"Cloudy",
    "location":"Minsk",
    "last_updated":"2023-12-06T21:30:00"
  }

2. Получение средней температуры за указанный период.
   URL: /weather/avg-temp

   Метод GET.

   Пример запроса:
   GET /weather/avg-temp?from=02-11-2023&to=06-11-2023

   обе даты в формате dd-MM-yyyy

   Пример ответа:
   {
     "avg_temp":"7.4"
   }

3. Для смены локации получения погоды, в файле src/main/resources/application.yml
   нужно поменять значение weather-api.location на нужное.
   Там же задается период, через который будут обновляться данные погоды: weather-api.fixed-rate.

4. Скрипты для создания схемы, а также для инициализация начальными данные в папке: src/main/resources/db/postgres.
   
