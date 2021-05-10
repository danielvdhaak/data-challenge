# Kafka streaming pub/sub system
Project in which I attempted to build a Kafka pub/sub system with Spark, NiFi and a database, all running using Docker Compose. Consists of:
* Producer sending random ratings (between 1 and 5) to a Kafka topic
* Apache NiFi template that reads the Netflix Movies and TV Shows .csv file ([link](https://www.kaggle.com/shivamb/netflix-shows "Kaggle")) containing 7787 records (working but unused)
* Streaming Consumer using Apache Spark that counts rating occurences in batches

## Used software
* Docker Desktop Mac (v20.10.6)
* Docker compose (v1.29.1)
* Java 8 (jdk1.8.0_291)
* Apache Maven (v3.8.1)

 ## Running the project
 Make sure Docker Desktop is running and current directory is the project root directory.

### Building
 1. Build the Java project.
```
mvn package assembly:single
```

2. Build Docker image.
```
docker build -t data-challenge .
```

### Starting containers
3. Start Docker Compose
```
docker-compose up -d
```

## Checking
The producer/consumer logs can be checked by opening the container in the `Containers/Apps` tab in Docker Desktop. 

4. Producer logs
```
docker logs data-challenge_kafka-producer_1 -f
```

5. Consumer logs
```
docker logs data-challenge_spark-consumer_1 -f
```

## Stopping
6. Stop Docker Compose
```
docker-compose down
```

## Apache NiFi
The NiFi web UI can be found at [http://localhost:8080/nifi](http://localhost:8080/nifi). The directory [/nifi/](../tree/nifi/) contains a template for loading a .csv file and publishing it to a Kafka topic. The [/nifi/ls-target](../tree/nifi/ls-target/) directory is mounted for storing and loading .csv files. 

This part is currently not used but can be used following some adaptions in the consumer code.





