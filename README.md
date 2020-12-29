# iot-event-serverapp
This project is a spring-boot web application using the RabbitMQ message broker to dispatch events within an IoT environment.

It uses a subscription feature in order to obtain IoT devices events' notifications.

## RabbitMQ
To fully test the subscription feature an instance of the RabbitMQ message broker is needed. The following snipet presents how to run rabbit's instance using Docker. 
```
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

## Subscription request
```
curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/subscriptions --data '{"subscriberId": "1001", "deviceName": "ESP_X1230"}'
```

## Creation of an event
```
curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/events --data '{"subject":"Temperatue event", "content":"Temperatue has changed to 20C.","createdAt":"2020-12-10T12:30:00","tags":[{"name":"temperature"}],"device":{"id":1000}}'
```
