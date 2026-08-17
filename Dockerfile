# Stage 1: Build
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Cache dependencies (This speeds up subsequent builds)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 3. Use a more specific COPY to avoid "multiple JARs" errors
# If you know your artifact ID, replace *.jar with that specific name
COPY --from=build /app/target/*.jar app.jar

# Render uses the PORT environment variable
EXPOSE 8080

# 4. Use exec form for better signal handling (SIGTERM)
# We still use sh -c to support the $PORT variable expansion
ENTRYPOINT ["sh", "-c", "java -Xmx400m -Xms400m -jar app.jar --server.port=${PORT:-8080}"]