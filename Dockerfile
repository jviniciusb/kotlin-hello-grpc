FROM openjdk:8
COPY . /src
WORKDIR /src
# RUN ./gradlew --no-daemon clean build

COPY ./build/libs/hello-grpc*.jar /usr/app/hello-grpc-app.jar
COPY ./*.pem /usr/app/
WORKDIR /usr/app
EXPOSE 6565
CMD ["java", "-jar", "hello-grpc-app.jar"]