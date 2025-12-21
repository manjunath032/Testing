# Docker usage for KafkaTest01

This guide helps you run the local Kafka stack via Docker Compose and perform basic topic operations. It uses the compose file at `src/main/resources/kafka-compose.yml` which brings up:
- Zookeeper (Confluent Platform)
- Kafka broker (Confluent Platform)
- Kafka UI (Provectus) at http://localhost:9080

Note on versions and listeners
- Zookeeper image: `confluentinc/cp-zookeeper:7.6.0`
- Kafka image: `confluentinc/cp-kafka:7.8.6`
- Kafka UI: `provectuslabs/kafka-ui:latest`
- The Kafka broker currently advertises `PLAINTEXT://localhost:9092`. This is perfect for host clients (your app on Windows) but Kafka UI (running in a container) may fail to connect unless the broker also advertises a container-network address (e.g., `kafka:29092`). See "Internal vs external access" below for a fix if needed.

## Prerequisites
- Docker Desktop installed and running
- Docker Compose v2 (invoked as `docker compose`)

## Verify Docker & Compose and validate the compose file

Run these from the project root or from any directory (paths adjusted below):

Windows cmd.exe (from project root):
```cmd
docker --version && docker compose version

docker compose -f .\src\main\resources\kafka-compose.yml config
```

If you prefer to run from the `src\main\resources` folder:
```cmd
cd /d .\src\main\resources

docker --version && docker compose version

docker compose -f kafka-compose.yml config
```

## Start the Kafka stack

From the project root:
```cmd
docker compose -f .\src\main\resources\kafka-compose.yml up -d
```

Check container status:
```cmd
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | findstr /I "kafka zookeeper kafka-ui"
```

Expected:
- `zookeeper` exposing 2181
- `kafka` exposing 9092
- `kafka-ui` exposing 9080 -> UI at http://localhost:9080

## Stop the Kafka stack

From the project root:
```cmd
docker compose -f .\src\main\resources\kafka-compose.yml down
```

## Create a topic (demo-topic)

There are two easy ways:

Option A: Enter the Kafka container shell, then run the command inside
```cmd
docker exec -it kafka bash
```
Inside the container shell:
```bashgit
kafka-topics --create \
  --topic demo-topic \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1
```

Option B: One-liner from Windows cmd.exe (no interactive shell)
```cmd
docker exec kafka bash -lc "kafka-topics --create --topic demo-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1"
```

Verify the topic exists:
```cmd
docker exec kafka bash -lc "kafka-topics --bootstrap-server localhost:9092 --list"
```

(Optional) Produce and consume test messages:
```cmd
REM Producer
docker exec -it kafka bash -lc "kafka-console-producer --bootstrap-server localhost:9092 --topic demo-topic"

REM Consumer
docker exec -it kafka bash -lc "kafka-console-consumer --bootstrap-server localhost:9092 --topic demo-topic --from-beginning --timeout-ms 10000"
```

## Internal vs external access (Kafka UI vs host applications)

Current compose config uses:
```yaml
environment:
  KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
  KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
```
This works well for host apps (e.g., your Spring app) connecting to `localhost:9092`.

However, `kafka-ui` (running in a container) is configured with `KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092`. Since the broker advertises `localhost:9092`, the UI may fail to connect. If Kafka UI can’t connect, consider switching to a dual-listener setup:

Example (not applied yet; you can update your compose file):
```yaml
environment:
  KAFKA_LISTENERS: PLAINTEXT_INTERNAL://0.0.0.0:29092,PLAINTEXT_EXTERNAL://0.0.0.0:9092
  KAFKA_ADVERTISED_LISTENERS: PLAINTEXT_INTERNAL://kafka:29092,PLAINTEXT_EXTERNAL://localhost:9092
  KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT_INTERNAL:PLAINTEXT,PLAINTEXT_EXTERNAL:PLAINTEXT
  KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT_INTERNAL
```
Then set Kafka UI to use `kafka:29092` for `KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS`.

## Notes
- If you want to align Confluent Platform versions, change Zookeeper to `confluentinc/cp-zookeeper:7.8.6` as well.
- Port 9080 on your host exposes Kafka UI at http://localhost:9080.
- If ports 9092/2181/9080 are busy, stop other services or remap ports in the compose file.

