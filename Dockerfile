FROM openjdk:18-jdk-slim

WORKDIR /app

# Копируем собранный JAR файл
COPY target/*.jar app.jar

# Создаем пользователя для безопасности
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser

# Меняем владельца файлов
RUN chown -R javauser:javauser /app
USER javauser

# Открываем порт
EXPOSE 8082

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"] 