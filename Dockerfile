FROM maven AS build

COPY src /app/src
COPY pom.xml /app
# RUN  mvn -f /app/pom.xml exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"
RUN  mvn -f /app/pom.xml clean compile assembly:single

FROM openjdk:11

RUN apt-get update \
    && apt-get install libglib2.0-0 -y \
    && apt-get install libnss3 -y \
    && apt-get install libnspr4 -y \
    && apt-get install libatk1.0-0 -y \
    && apt-get install libatk-bridge2.0-0 -y \
    && apt-get install libcups2 -y \
    && apt-get install libdrm2 -y \
    && apt-get install libdbus-1-3 -y \
    && apt-get install libexpat1 -y \
    && apt-get install libxcb1 -y \
    && apt-get install libxkbcommon0 -y \
    && apt-get install libx11-6 -y \
    && apt-get install libxcomposite1 -y \
    && apt-get install libxdamage1 -y \
    && apt-get install libxext6 -y \
    && apt-get install libxfixes3 -y \
    && apt-get install libxrandr2 -y \
    && apt-get install libgbm1 -y \
    && apt-get install libgtk-3-0 -y \
    && apt-get install libpango-1.0-0 -y \
    && apt-get install libcairo2 -y \
    && apt-get install libasound2 -y \
    && apt-get install libatspi2.0-0 -y \
    && apt-get install libxshmfence1 -y

COPY tasks /tasks
COPY templates /templates
COPY config.properties /config.properties
COPY --from=build /app/target/marktplaats_scraper.jar marktplaats_scraper.jar
ENTRYPOINT ["java", "-jar","marktplaats_scraper.jar"]

