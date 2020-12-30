FROM maven:3.6.3-jdk-8

COPY . /server

WORKDIR /server

RUN mvn verify

ENTRYPOINT java -jar target/compiler-0.0.1-SNAPSHOT.jar

EXPOSE 9090:9090
