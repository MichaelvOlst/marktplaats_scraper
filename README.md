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
or 
docker run --rm -it -v $(pwd)/storage:/storage scraper:latest
```
### Things to fix
- [ ] Clean files older than x days
- [ ] Notify the users by email
- [x] Attach the volume storage in the dockerfile
- [x] Implement logging system with log4j2