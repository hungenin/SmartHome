FROM openjdk:11-slim
WORKDIR /app
COPY ./target/SmartHome-0.0.1-SNAPSHOT.jar /app/smartHome.jar
CMD "java" "-jar" "/app/smartHome.jar"