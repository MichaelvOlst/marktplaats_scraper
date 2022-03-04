FROM maven AS build

COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean compile assembly:single

FROM openjdk:8
COPY tasks /tasks
COPY --from=build /app/target/marktplaats_scraper.jar marktplaats_scraper.jar
ENTRYPOINT ["java", "-jar","marktplaats_scraper.jar"]