FROM openjdk:17-jdk-slim

WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Создаем пользователя для безопасности
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser

# Копируем JAR файл
COPY target/*.jar app.jar

# Меняем владельца файлов
RUN chown -R javauser:javauser /app
USER javauser

# Открываем порт
EXPOSE 8082

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"] 