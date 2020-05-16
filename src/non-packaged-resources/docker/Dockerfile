# Dockerfile
FROM java:8-jdk-alpine
COPY ./app/hm_mp.jar /usr/app/

WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "hm_mp.jar"]
CMD ["--spring.profiles.active=test"]
