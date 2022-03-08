# Marktplaats scraper

I created this in Java and Maven so I can learn how to program in this language. 
If you think something is missing or the code can be better, Let me know or create a pull request.
Oh and maybe this is import, but I used jdk 11.

It scrapes marktplaats for new items and notifies you by email. 
You can create a task which should be in json, in the task you can define the interval (which is in seconds) and to which email adress it should go and so on. You can copy the task that is already in the "tasks" folder if you would like to get started or you could simply create one yourself with the code below.

### Email template
The given selectors are also available in the email template, like so {{key}}. 
So if you would like to customize the template and make use of more selectors or less.
You can also see an example in the templates folder.

### Config
There is also a config.properties.example file in the root. You should copy this and use your own credentials or use the command below.

```
mv config.properties.example config.properties
```

### Example task
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
  "interval": 600 // is in seconds.
}
```

### Commands for compiling and running the jar
```
mvn clean compile assembly:single
java -jar target/marktplaats_scraper.jar
```

### Run project in Docker

First you have to build it
```
docker build -t scraper .
```

Then you can use any of these three commands below

#### Without the storage folder
```
docker run --rm -it scraper:latest
```
#### With the storage folder
```
docker run --rm -it -v $(pwd)/storage:/storage scraper:latest
```

#### Run docker in the background with the storage folder attached
```
docker run --rm -it -v $(pwd)/storage:/storage -d scraper:latest
```