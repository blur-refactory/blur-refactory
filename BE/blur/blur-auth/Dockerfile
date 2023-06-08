FROM openjdk:11-slim
WORKDIR /usr/src/app

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
CMD ["java","-jar","app.jar"]
