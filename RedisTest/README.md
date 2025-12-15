 # RedisTest

This project is a small Spring Boot (Java 21) application demonstrating Redis-based caching (compatible with Redis 7).

What I added
- Spring Boot app (`RedisTestApplication`) with caching enabled
- `RedisConfig` (CacheManager using JSON serializer, 10 min TTL)
- `UserService` with @Cacheable / @CacheEvict
- `UserController` exposing REST endpoints
- `docker-compose.yml` to run a local Redis 7 container
- `Dockerfile` to build a runtime image of the Spring Boot app
- Integration test using Testcontainers (requires Docker)
- `.gitignore`

Quick goals/checklist
- [x] Buildable Spring Boot app (Java 21)
- [x] Redis caching via Spring Data Redis
- [x] REST endpoints to exercise cache
- [x] Docker Compose for Redis
- [x] Integration test scaffold (Testcontainers)

How to run (Windows, cmd.exe)

1) Start Redis locally with Docker Compose (recommended for dev):

```cmd
cd /d F:\Projects\Testing\RedisTest
docker-compose up -d
```

2) Build the application jar (skip tests to be fast):

```cmd
cd /d F:\Projects\Testing\RedisTest
mvn -DskipTests package
```

3) Run the app (it will connect to localhost:6379 by default):

```cmd
java -jar target\RedisTest-1.0.0-SNAPSHOT.jar
```

4) Test the endpoints (measure timings to observe caching):

First call (cold cache) — should be slower (simulated 500ms):

```cmd
curl -s -o NUL -w "%%{time_total}\n" http://localhost:8080/api/users/1
```

Get response body (JSON):

```cmd
curl http://localhost:8080/api/users/1
```

Second call (cached) — should be much faster:

```cmd
curl -s -o NUL -w "%%{time_total}\n" http://localhost:8080/api/users/1
```

Evict cache for id=1:

```cmd
curl -X DELETE http://localhost:8080/api/users/1 -i
```

Notes about Docker/image
- Build the app image (after packaging):

```cmd
cd /d F:\Projects\Testing\RedisTest
docker build -t redistest:local .
```

- Run with a Redis container using docker-compose (preferred) or link manually.

Integration tests
- The project includes a Testcontainers-based integration test (`UserServiceIntegrationTest`).
- To run tests you need Docker Desktop running. Run:

```cmd
cd /d F:\Projects\Testing\RedisTest
mvn test
```

About the earlier build/check output
- I ran `mvn -DskipTests package` and the project successfully compiled and produced `target\RedisTest-1.0.0-SNAPSHOT.jar`.
- The IDE-style error checker reported some "Cannot resolve symbol" and "Package name does not correspond to the file path" warnings. These are from the static analysis tool that isn't using the Maven classpath / module settings correctly; they don't reflect the actual Maven build (which succeeded). If your IDE shows these, re-import the project as a Maven project (or refresh Maven dependencies) so the IDE picks up the Gradle/Maven classpath.

Next recommended steps
- Start Redis via docker-compose and run the app locally and test with the curl commands above.
- If you want, I can also:
  - Add a small health-check endpoint
  - Wire graceful shutdown config
  - Add metrics (Micrometer) and README examples
  - Add CI pipeline (GitHub Actions) that builds, runs tests, and builds the Docker image

If you'd like, I can run the integration tests now (requires Docker Desktop running). What would you like me to do next?
