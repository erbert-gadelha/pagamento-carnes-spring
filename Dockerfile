# Etapa 1: Build
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app

# Copiar apenas arquivos Gradle para cache de dependências
COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon || return 0

# Copiar o restante do código e construir a aplicação
COPY . .
RUN ./gradlew bootJar -x test --no-daemon --parallel

# Etapa 2: Executar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

