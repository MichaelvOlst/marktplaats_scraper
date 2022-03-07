# marktplaats_scraper
It scrapes marktplaats for new items and notifies you by email. 
You can create a task which should be in json, in the task you can define the interval and to which email adress it should go and so on. You can copy the task that is already in the "tasks" folder if you would like to get started or you could simply create one yourself with the code below. 
I created this in Java and Maven so I can learn how to program in this language. 
If you think something is missing or the code can be better, Let me know or create a pull request.


### example task
```
{
  "title": "Ford Focus Station",
  "url": "https://www.marktplaats.nl/l/auto-s/ford/f/focus+benzine/784+473/#f:484|constructionYearFrom:2019|sortBy:SORT_INDEX|sortOrder:DECREASING",
  "itemHolder": ".mp-Listing--list-item",
  "itemHref": ".mp-Listing-coverLink",
  "selectors": {
    "title": ".mp-Listing-title",
    "price": ".mp-Listing-price",
    "kilometers": ".mp-Listing-attributes-nap-mileage",
    "year": ".mp-Attribute--default >> nth=0",
    "seller": ".mp-Listing-seller-name"
  },
  "email": {
    "from": "michaelvolst@gmail.com",
    "to": "michaelvolst@gmail.com",
    "title": "Nieuwe Ford Focus Station gevonden"
  },
  "interval": 30
}
```

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
or to run docker in the background you can use this command
docker run --rm -it -v $(pwd)/storage:/storage -d scraper:latest
```
### Things to fix
- [x] Clean files older than x days
- [x] Parse the mail template and style it
- [x] Notify the users by email
- [x] Separate the logic for storing and scraping. The scraper should only scrape and return the results.
- [x] Attach the volume storage in the dockerfile
- [x] Implement logging system with log4j2