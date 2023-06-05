FROM openjdk:11-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/blur-discovery-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get install -y tzdata

# ./gradlew clean build 먼저 시행
# docker build --tag gangesriver/blur-discover:0.1 .
# docker push gangesriver/blur-discover:0.1
# docker pull gangesriver/blur-discover:0.1
# docker run --rm -d -p 8761:8761 --network ecommerce-network --name blur-discover gangesriver/blur-discover:0.1