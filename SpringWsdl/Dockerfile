# Runtime-only Dockerfile. Build the JAR in CI (or locally) with `mvn -B -DskipTests package` then use this Dockerfile to produce the runtime image.
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the already-built JAR from target produced by CI or local mvn package
COPY target/SpringWsdl-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
