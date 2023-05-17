# Drone operation API

## Описание

Набор API для управления грузовыми дронами

##Необходимое ПО
* Docker
* Docker compose

## Запуск
* Создайте образ приложения с помощью следующей команды
```
mvn jib:dockerBuild
```
* Выполните запуск приложения и БД через docker-compose с помощью следующей команды
```
docker-compose up
```

После запуска compose файла, будут созданы два контейнера:
- drones_application
- database with PostgreSQL

Доступ к API приложения осуществляется через порт 8080 хост-машины.\
Вы можете подключиться к контейнеру базы данных через порт 5001\
Доступ из swagger осуществляется по ссылке: http://localhost:8080/swagger-ui.html

В приложении реализована базовая симуляция отправки грузов с помощью планировщиков.\
При переходе дрона в статус DELIVERING планировщик меняет статусы дрона через определенный интервал и фиксирует расход заряда:
- DELIVERING -> DELIVERED - интервал 5 секунд, расход заряда 8%
- DELIVERED -> RETURNING - интервал 5 секунд, расход заряда 9%
- RETURNING -> IDLE - интервал 5 секунд, расход заряда 8%\
  В состоянии IDLE дроны заряжаются на 5% каждые 7 секунд.

Реализован дополнительный планировщик, выводящий состояние заряда дронов в лог каждые 10 секунд.



## Набор методов

## drone-controller - /api/drones

## GET /api/drones/find-available-drones-for-loading

Возвращает список дронов, доступных для загрузки

Пример исходящего JSON  (code 200)
```
[
  {
    "cargo": [
      {
        "medication": {
          "code": "string",
          "image": "string",
          "name": "string",
          "weight": 0
        },
        "quantity": 0,
        "weight": 0
      }
    ],
    "model": "Lightweight",
    "percentage": 0,
    "serialNumber": "string",
    "state": "IDLE",
    "weight": 0
  }
]
```


## GET /api/drones/get-cargo-by-drone-serial-number?serialNumber="serialNumber"

Возвращает список грузов по серийному номеру дрона

Пример исходящего JSON (code 200)
```
[
  {
    "quantity": 4,
    "weight": 120,
    "medication": {
      "name": "tourniquet",
      "weight": 30,
      "code": "TRQ",
      "image": "path/to/image.png"
    }
  },
  {
    "quantity": 3,
    "weight": 90,
    "medication": {
      "name": "syringe",
      "weight": 30,
      "code": "SRG",
      "image": "path/to/image.png"
    }
  }
]
```
Пример исходящего JSON (code 404)

```
{
  "message": "Drone with serial number DR-10 is not found"
}
```

## GET /api/drones/get-percentage-by-drone-serial-number?serialNumber="serialNumber"

Возвращает процент заряда дрона по переданному серийному номеру

Пример исходящего JSON (code 200)

```
{
  "serialNumber": "DR-02",
  "message": "percentage = 100"
}
```
Пример исходящего JSON (code 404)

```
{
  "message": "Drone with serial number DR-10 is not found"
}
```

## POST /api/drones/load-medicine

Загружает медикаменты в дрон

Пример входящего JSON:

```
{
  "droneSerialNumber": "DR-01",
  "medicationRequests": [
      {
       "medicationCode": "BND",
       "quantity": 2
      }
    ]
}
```

Пример исходящего JSON (code 200)
```
{
  "serialNumber": "DR-01",
  "message": "Drone loaded successfully"
}
```
Пример исходящего JSON (code 404)
```
{
  "message": "Medicine is not found by codes: [BND1]"
}
```

## POST /api/drones/register

Регистрация нового дрона

Пример входящего JSON:
```
{
  "model": "Lightweight",
  "percentage": 0,
  "serialNumber": "string",
  "state": "IDLE",
  "weight": 0
}
```
Пример исходящего JSON (code 201)
```
{
  "cargo": null,
  "serialNumber": "DR-06",
  "model": "Lightweight",
  "weight": 10,
  "percentage": 0,
  "state": "IDLE"
}
```
Пример исходящего JSON (code 400)
```
{
  "weight": "Weight cannot be less than 1"
}
```

## POST /api/drones/send-drone?serialNumber="serialNumber"

Отправка дрона для доставки

Пример исходящего JSON (code 200)
```
{
  "serialNumber": "DR-01",
  "message": "Drone sent successfully"
}
```

Пример исходящего JSON (code 400)
```
{
  "message": "The drone must be in the LOADING state to start the delivery process. Current state: IDLE"
}
```

Пример исходящего JSON (code 404)
```
{
  "message": "Drone with serial number DR-011 is not found"
}
```

## medication-controller - /api/medication
## GET /api/medication/find-all

Получить список всех медикаментов

Пример исходящего JSON (code 200)
```
[
  {
    "code": "string",
    "image": "string",
    "name": "string",
    "weight": 0
  }
]
```

## POST /api/medication/add

Добавить медикамент

Пример входящего JSON:
```
{
  "code": "MED",
  "image": "path/to/image.png",
  "name": "MED",
  "weight": 1
}
```

Пример исходящего JSON (code 200)
```
{
  "name": "MED",
  "weight": 1,
  "code": "MED",
  "image": "path/to/image.png"
}
```

Пример исходящего JSON (code 400)
```
{
  "image": "Image should contain only path",
  "code": "Code should contain only upper case letters, numbers, '_'",
  "name": "Name should contain only letters, numbers, '-', '_'",
  "weight": "Weight should not be less than 0"
}
```

# Drone operation API

## Description

Drone control API set

##Requirements
* Docker
* Docker compose

## Execution
* Build image of application by following command
```
mvn jib:dockerBuild
```
* Execute docker compose by following command
```
docker-compose up
```

After running the compose file, two containers will be created:
- drones_application
- database with PostgreSQL

Application API is accessed on port 8080 of the host machine.\
You can connect to the database container on port 5001\
Access from swagger is carried out by the link: http://localhost:8080/swagger-ui.html

The application implements a basic simulation of sending cargos using schedulers.\
When the drone enters the DELIVERING status, the scheduler changes the drone statuses at a certain interval and fixes the charge consumption:
- DELIVERING -> DELIVERED - interval 5 seconds, charge consumption 8%
- DELIVERED -> RETURNING - interval 5 seconds, charge consumption 9%
- RETURNING -> IDLE - interval 5 seconds, charge consumption 8%\
In IDLE status, drones charge 5% in 7 seconds

An additional scheduler has been implemented that writes the state of charge of drones to the log every 10 seconds



## Methods set

## drone-controller - /api/drones

## GET /api/drones/find-available-drones-for-loading

Return list of drones available for loading

JSON output example (code 200)
```
[
  {
    "cargo": [
      {
        "medication": {
          "code": "string",
          "image": "string",
          "name": "string",
          "weight": 0
        },
        "quantity": 0,
        "weight": 0
      }
    ],
    "model": "Lightweight",
    "percentage": 0,
    "serialNumber": "string",
    "state": "IDLE",
    "weight": 0
  }
]
```


## GET /api/drones/get-cargo-by-drone-serial-number?serialNumber="serialNumber"

Retrieve list of cargo for a given serial number

JSON output example (code 200)
```
[
  {
    "quantity": 4,
    "weight": 120,
    "medication": {
      "name": "tourniquet",
      "weight": 30,
      "code": "TRQ",
      "image": "path/to/image.png"
    }
  },
  {
    "quantity": 3,
    "weight": 90,
    "medication": {
      "name": "syringe",
      "weight": 30,
      "code": "SRG",
      "image": "path/to/image.png"
    }
  }
]
```
JSON output example (code 404)

```
{
  "message": "Drone with serial number DR-10 is not found"
}
```

## GET /api/drones/get-percentage-by-drone-serial-number?serialNumber="serialNumber"

Retrieve percentage for a given serial number

JSON output example (code 200)

```
{
  "serialNumber": "DR-02",
  "message": "percentage = 100"
}
```
JSON output example (code 404)

```
{
  "message": "Drone with serial number DR-10 is not found"
}
```

## POST /api/drones/load-medicine

Load medicine into drone

JSON input example:

```
{
  "droneSerialNumber": "DR-01",
  "medicationRequests": [
      {
       "medicationCode": "BND",
       "quantity": 2
      }
    ]
}
```

JSON output example (code 200)
```
{
  "serialNumber": "DR-01",
  "message": "Drone loaded successfully"
}
```
JSON output example (code 404)
```
{
  "message": "Medicine is not found by codes: [BND1]"
}
```

## POST /api/drones/register

Register drone

JSON input example:
```
{
  "model": "Lightweight",
  "percentage": 0,
  "serialNumber": "string",
  "state": "IDLE",
  "weight": 0
}
```
JSON output example (code 201)
```
{
  "cargo": null,
  "serialNumber": "DR-06",
  "model": "Lightweight",
  "weight": 10,
  "percentage": 0,
  "state": "IDLE"
}
```
JSON output example (code 400)
```
{
  "weight": "Weight cannot be less than 1"
}
```

## POST /api/drones/send-drone?serialNumber="serialNumber"

Send drone for delivering

JSON output example (code 200)
```
{
  "serialNumber": "DR-01",
  "message": "Drone sent successfully"
}
```

JSON output example (code 400)
```
{
  "message": "The drone must be in the LOADING state to start the delivery process. Current state: IDLE"
}
```

JSON output example (code 404)
```
{
  "message": "Drone with serial number DR-011 is not found"
}
```

## medication-controller - /api/medication
## GET /api/medication/find-all

Get a list of all medicine

JSON output example (code 200)
```
[
  {
    "code": "string",
    "image": "string",
    "name": "string",
    "weight": 0
  }
]
```

## POST /api/medication/add

Add medication

JSON input example:
```
{
  "code": "MED",
  "image": "path/to/image.png",
  "name": "MED",
  "weight": 1
}
```

JSON output example (code 200)
```
{
  "name": "MED",
  "weight": 1,
  "code": "MED",
  "image": "path/to/image.png"
}
```

JSON output example (code 400)
```
{
  "image": "Image should contain only path",
  "code": "Code should contain only upper case letters, numbers, '_'",
  "name": "Name should contain only letters, numbers, '-', '_'",
  "weight": "Weight should not be less than 0"
}
```