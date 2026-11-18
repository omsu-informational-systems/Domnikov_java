# Первый этап сборки компилирует код
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

# Кэшируем локальный репозиторий Maven
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline


COPY src ./src

# Собираем приложение с использованием кэша зависимостей
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn package -DskipTests



# Второй этап сборки запускает jar-ник
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Команды, которые выполняются при старте контейнера
CMD ["java", "-jar", "app.jar"]