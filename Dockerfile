FROM maven:3.9-eclipse-temurin-26-noble AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package

FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/target/classes ./
ENTRYPOINT ["java", "-cp", "/app", "Main"]