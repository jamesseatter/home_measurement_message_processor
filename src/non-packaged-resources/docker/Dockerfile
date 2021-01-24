# Dockerfile
FROM openjdk:11-jre-slim
COPY ./hm_mp.jar /usr/app/

WORKDIR /usr/app
ENTRYPOINT ["java", "-Dspring.profiles.active=qa", "-jar", "hm_mp.jar"]
