FROM openjdk:11-jre
COPY target/spring-cloud-in-practice-gateway-1.0.0-SNAPSHOT.jar /app.jar

VOLUME [ "/data" ]
EXPOSE 8080
RUN mkdir -p /data/log
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
