FROM openjdk:8
ADD target/bicycle_scraper.jar bicycle_scraper.jar
ENTRYPOINT ["java", "-jar","bicycle_scraper.jar"]
EXPOSE 8080