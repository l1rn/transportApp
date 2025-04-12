FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

RUN apk add --no-cache bash dos2unix

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN dos2unix mvnw && chmod +x mvnw

RUN ./mvnw dependency:go-offline -B
COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]