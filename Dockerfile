FROM openjdk:8
ADD target/marktplaats_scraper.jar marktplaats_scraper.jar
ENTRYPOINT ["java", "-jar","marktplaats_scraper.jar"]
EXPOSE 8080