# Kafka streaming pub/sub system
Project in which I attempted to build a Kafka pub/sub system with Spark and a database, all running using Docker Compose. Consists of:
* Producer sending random ratings (between 1 and 5) to a Kafka topic
* Streaming Consumer using Apache Spark that counts rating occurences in batches

## Used software
* Docker Desktop Mac (v20.10.6)
* Docker compose (v1.29.1)
* Java 8 (jdk1.8.0_291)
* Apache Maven (v3.8.1)

 ## Running the project
 Make sure Docker Desktop is running and current directory is the project root directory.

 1. Build the Java project.
```
mvn package assembly:single
```

2. Start Docker Compose
```
docker-compose up -d
```

## Checking
The producer/consumer logs can be checked by opening the container in the `Containers/Apps` tab in Docker Desktop. 

3. Producer logs
```
docker logs data-challenge_kafka-producer_1 -f
```

4. Consumer logs
```
docker logs data-challenge_spark-consumer_1 -f
```

## Stopping
5. Stop Docker Compose
```
docker-compose down
```