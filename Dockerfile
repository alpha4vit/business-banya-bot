FROM openjdk:21

# Устанавливаем временную зону +03:00
ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Создаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл приложения в контейнер
COPY target/*.jar app.jar

EXPOSE 8080
# Запускаем приложение при старте контейнера
ENTRYPOINT ["java", "-Xms1024m", "-Xmx3072m", "-jar", "/app/app.jar"]