# Dockerfile
FROM openjdk:11-jre-slim
COPY ./app/hm_mp.jar /usr/app/

WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "hm_mp.jar"]
CMD ["--spring.profiles.active=test"]
