# Etapa 1: Build
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon || return 0
COPY . .
RUN ./gradlew bootJar -x test --no-daemon --parallel

# Etapa 2: Executar
FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y haveged && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

