# ---------- Build stage ----------
FROM gradle:8.10-jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew bootJar --no-daemon


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
