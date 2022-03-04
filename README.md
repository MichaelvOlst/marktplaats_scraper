# simple_scraper

### Command to run for compiling into a fat jar

```
mvn clean compile assembly:single
```
### Command to run for calling the jar file

```
java -jar target/marktplaats_scraper.jar
```

### Run project in Docker

```
docker build -t scraper .
docker run --rm -it scraper:latest
```
