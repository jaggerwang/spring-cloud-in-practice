FROM openjdk:11-jre

WORKDIR /app
COPY target/spring-cloud-in-practice-stat-1.0.0-SNAPSHOT.jar ./
RUN mkdir config
RUN mkdir -p /data/log

VOLUME ["/data"]

EXPOSE 8080

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "spring-cloud-in-practice-stat-1.0.0-SNAPSHOT.jar"]
