FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/music-0.0.1-SNAPSHOT.jar /app/music.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/music.jar"]
