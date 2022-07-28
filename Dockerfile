FROM maven:3-eclipse-temurin-17-alpine

WORKDIR /app

COPY . .

RUN mvn install -DskipTests
ENTRYPOINT mvn install -DskipTests && java -jar ./target/*.jar