FROM openjdk:11-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/blur-apigateway-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} BlurApigateway.jar
ENTRYPOINT ["java","-jar","/BlurApigateway.jar"]
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get install -y tzdata

# ./gradlew clean build 먼저 시행
# docker build --tag gangesriver/blur-apigateway:0.1 .
# docker push gangesriver/blur-apigateway:0.1
# docker pull gangesriver/blur-apigateway:0.1
# docker run --rm -d -p 8000:8000 --network ecommerce-network --name blur-apigateway -e "eureka.client.serviceUrl.defaultZone=http://blur-discover:8761/eureka/" gangesriver/blur-apigateway:0.1