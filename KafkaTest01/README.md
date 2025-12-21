# KafkaTest01

A minimal Spring Boot (3.3.x) + Spring for Apache Kafka sample that:
- Exposes a simple REST endpoint to publish messages to a Kafka topic
- Consumes the same topic with manual acknowledgments
- Includes a Docker Compose file to spin up Kafka + Zookeeper locally

Tested with Java 21.

## Project layout

- `src/main/java/org/example/`
  - `SpringKafkaTest01Application.java` — Spring Boot main application
  - `KafkaController.java` — REST endpoint to send messages: `GET /kafka/send?message=...`
  - `KafkaProducerService.java` — Producer using `KafkaTemplate` (topic from config)
  - `KafkaConsumerConfig.java` — Configures listener container factory (manual Ack mode)
  - `KafkaConsumerListener.java` — Consumer that manually acknowledges after processing
  - `KafkaConsumerListener_old.java` — Simple consumer example (disabled by `@Profile("!old")`)
- `src/main/resources/`
  - `application.yml` — App configuration (Kafka bootstrap, topic, group)
  - `kafka-compose.yml` — Docker Compose for local Kafka + Zookeeper
- `src/main/webapp/`
  - `index.jsp`, `WEB-INF/web.xml` — present from archetype; the primary API is the REST controller.

## Requirements

- Windows with Docker Desktop (for local Kafka via Docker Compose)
- Java 21
- Maven 3.9+

## Configuration (application.yml)

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      group-id: demo-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer

kafka:
  topic:
    demo: demo-topic
  group:
    demo: demo-group
```

- Producer sends to the topic from `kafka.topic.demo`.
- Consumer listens to the same topic (`@KafkaListener(topics = "${kafka.topic.demo}", groupId = "${kafka.group.demo}")`).
- Manual Ack is enabled (`ContainerProperties.AckMode.MANUAL`) — if processing throws, the offset is not committed (message can be redelivered).

## Start Kafka locally (Docker Compose)

From the project root directory, bring up Zookeeper and Kafka:

```cmd
docker compose -f .\src\main\resources\kafka-compose.yml up -d

REM Optional: verify containers
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | findstr /I "kafka zookeeper"
```

- Kafka should be reachable on `localhost:9092`.
- Zookeeper should be on `localhost:2181`.

Stop/clean up:

```cmd
docker compose -f .\src\main\resources\kafka-compose.yml down
```

## Build the application

Run from the project root:

```cmd
mvn clean package -DskipTests
```

- Output: `target\KafkaTest01.war` (Spring Boot repackaged WAR)

## Run the application

Option 1: via Maven

```cmd
mvn spring-boot:run
```

Option 2: via the packaged artifact

```cmd
java -jar .\target\KafkaTest01.war
```

The app starts on `http://localhost:8080` by default.

## Publish a message (REST)

Send a message to Kafka via the REST endpoint:

```cmd
curl "http://localhost:8080/kafka/send?message=hello-from-rest"
```

Expected response:
```
Message sent to Kafka
```

Watch the application logs; you should see the consumer print the payload:
```
Consumed message : hello-from-rest
```

## Verify Kafka with console tools (optional)

Run commands inside the Kafka container:

```cmd
REM List topics
docker exec -it kafka bash -lc "kafka-topics --bootstrap-server localhost:9092 --list"

REM Create the demo topic, if needed
docker exec -it kafka bash -lc "kafka-topics --bootstrap-server localhost:9092 --create --topic demo-topic --partitions 1 --replication-factor 1"

REM Produce manually
docker exec -it kafka bash -lc "kafka-console-producer --bootstrap-server localhost:9092 --topic demo-topic"

REM In another terminal, consume from beginning
docker exec -it kafka bash -lc "kafka-console-consumer --bootstrap-server localhost:9092 --topic demo-topic --from-beginning --timeout-ms 10000"
```

## Troubleshooting

- Docker/Compose not found or no output
  - Start Docker Desktop, then re-run the compose commands.
- Connection to Kafka refused from the app
  - Ensure compose stack is running and port `9092` is free.
  - Confirm `spring.kafka.bootstrap-servers` is set to `localhost:9092` (matches compose).
- Messages not being consumed / offset not committed
  - Consumer uses manual Ack. Ensure `KafkaConsumerListener.process(...)` doesn’t throw. If it does, the offset is not acknowledged and the message may be redelivered.
- Change topic or group
  - Update `kafka.topic.demo` and `kafka.group.demo` in `application.yml`. The REST endpoint/producer uses these values dynamically.

## Versions

- Spring Boot: 3.3.4
- Spring Kafka: 3.2.4
- Java: 21
- Kafka (Docker images): Confluent Platform 7.6.0

## Notes

- The project is packaged as a Boot WAR but runs as an executable with the embedded Tomcat via `java -jar`. The JSP/web.xml files are from the original archetype; the primary interface is the REST endpoint.
- For production, replace `System.out.println` with a logging framework (SLF4J/Logback) and add structured logging/MDC as needed.
