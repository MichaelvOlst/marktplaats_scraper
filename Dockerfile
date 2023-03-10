FROM maven AS build

COPY src /app/src
COPY pom.xml /app
# RUN  mvn -f /app/pom.xml exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"
RUN  mvn -f /app/pom.xml clean compile assembly:single

FROM openjdk:11

RUN apt-get update \
    && apt-get install -y \
    libglib2.0-0 \
    libnss3 \
    libnspr4 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libdbus-1-3 \
    libexpat1 \
    libxcb1 \
    libxkbcommon0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libgtk-3-0 \
    libpango-1.0-0 \
    libcairo2 \
    libasound2 \
    libatspi2.0-0 \
    libxshmfence1

COPY tasks /tasks
COPY templates /templates
COPY config.properties /config.properties
COPY --from=build /app/target/marktplaats_scraper.jar marktplaats_scraper.jar
ENTRYPOINT ["java", "-jar", "marktplaats_scraper.jar"]

