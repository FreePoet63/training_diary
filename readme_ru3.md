## TASK 2
Краткое описание
В проект добавлены aspectj, servlets.

Приложение использует клиент - серверную архитектуру для отправки запросов Training Diary.

Audit логгирует все методы Servlets и Services.

Для запуска используется Jetty - сервер. Приложение запускается из класса StartApplication, запускает сервер, и можно отправлять endpoints используя API - инструменты, например POSTMAN.

### Endpoints:

#### User:

POST http://localhost::8099/users

     {
         "name": "name",
         "password": "password"
      }

GET http://localhost::8099/users/login?name={name}&password={password}

GET http://localhost::8099/users/{id}

GET http://localhost::8099/users

#### Training Diary:

Только для авторизированных пользователей, для отправки запросов нужна авторизация.

POST: http://localhost::8099/workout

example body:

        {    
            "type": "HIGH_INTENSITY_INTERVAL_TRAINING",
            "duration": 200,
            "caloriesBurned": 450,
            "params": [
                        {
                          "params": "Interval frequency",
                          "value": 15
                         }
                      ]
        }

PUT: http://localhost::8099/workout/{id}

example body:

        { 
            "type": "HIGH_INTENSITY_INTERVAL_TRAINING",
            "duration": 200,
            "caloriesBurned": 450,
            "params": [
                        {
                          "params": "Interval frequency",
                          "value": 15
                         }
                      ]
        }


DELETE http://localhost::8099/workout/{id}

GET http://localhost::8099/workout?date={date}

example date = 2024-01-30T22:16:01

GET http://localhost::8099/workout/params?type={type}&startDate={startDate}&endDate={startDate}

example type=DANCE and startDate = 2024-01-30T22:16:01 and endDate = 2024-04-30T22:16:01

GET http://localhost::8099/workout/calories?startDate={startDate}&endDate={startDate}

example startDate = 2024-01-30T22:16:01 and endDate = 2024-04-30T22:16:01

GET http://localhost::8099/workout/all

данный запрпос доступен только пользователям ADMIN

Проект покрыт тестами на 76 ппоцентов.
