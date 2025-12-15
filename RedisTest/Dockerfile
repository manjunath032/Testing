# Simple Dockerfile - expects a built jar at target/
FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/RedisTest-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

