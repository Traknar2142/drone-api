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
- prometheus
- grafana
- zookeeper
- kafka

Доступ к API приложения осуществляется через порт 8080 хост-машины.\
Вы можете подключиться к контейнеру базы данных через порт 5001\
Доступ из swagger осуществляется по ссылке: http://localhost:8080/swagger-ui.html/ \
Доступ к prometheus по ссылке: http://localhost:9090/ \
Доступ к grafana по ссылке: http://localhost:3000/ 

Рекомендую импортировать файл дешбордов springboot-dashboard.json в графану для корректного отобрадения метрик

В приложении реализована базовая симуляция отправки грузов с помощью планировщиков.\
При переходе дрона в статус DELIVERING планировщик меняет статусы дрона через определенный интервал и фиксирует расход заряда:
- DELIVERING -> DELIVERED - интервал 5 секунд, расход заряда 8%
- DELIVERED -> RETURNING - интервал 5 секунд, расход заряда 9%
- RETURNING -> IDLE - интервал 5 секунд, расход заряда 8%\
  В состоянии IDLE дроны заряжаются на 5% каждые 7 секунд.

Реализован дополнительный планировщик, выводящий состояние заряда дронов в лог каждые 10 секунд.



## Набор методов

## order-controller - /api/orders

## POST /api/orders/make-order
Позволяет сделать заказ необходимых медикаментов \
Поддержано ограничение на вес в 500 грамм для каждого дрона
Для доставки отбираются дроны в статусе IDLE и зарядом более 25%


Пример входящего JSON:

```
{
  "customer": {
    "address": {
      "city": "someAddress",
      "street": "SomeStreet"
    },
    "name": "someName"
  },
  "medicationRequests": [
    {
      "medicationCode": "BND",
      "quantity": 8
    },
    {
      "medicationCode": "ASP",
      "quantity": 2
    },
    {
      "medicationCode": "SRG",
      "quantity": 2
    },
    {
      "medicationCode": "TRQ",
      "quantity": 2
    }
  ]
}
```

Пример исходящего JSON (code 200)

```
{
  "message": "request accepted for processing"
}
```

## GET /api/orders/get-orders-by-customer-name

Возвращает список заказов клиента по имени клиента

Пример исходящего JSON (code 200)
```
[
  {
    "customer": {
      "name": "name",
      "address": {
        "city": "someAddress",
        "street": "SomeStreet",
        "id": 10
      },
      "id": 2
    },
    "cargos": [
      {
        "id": 44,
        "quantity": 2,
        "weight": 60,
        "medication": {
          "id": 3,
          "name": "syringe",
          "weight": 30,
          "code": "SRG",
          "image": "path/to/image.png"
        }
      },
      {
        "id": 41,
        "quantity": 2,
        "weight": 100,
        "medication": {
          "id": 2,
          "name": "aspirin",
          "weight": 50,
          "code": "ASP",
          "image": "path/to/image.png"
        }
      },
      {
        "id": 42,
        "quantity": 8,
        "weight": 400,
        "medication": {
          "id": 1,
          "name": "bandage",
          "weight": 50,
          "code": "BND",
          "image": "path/to/image.png"
        }
      },
      {
        "id": 43,
        "quantity": 2,
        "weight": 60,
        "medication": {
          "id": 4,
          "name": "tourniquet",
          "weight": 30,
          "code": "TRQ",
          "image": "path/to/image.png"
        }
      }
    ],
    "id": 10
  }
]
```
Пример исходящего JSON (code 404)

```
{
  "message": "Orders are not found by given customer's name: name1"
}
```

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