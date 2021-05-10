package com.danielvdhaak;

import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.Durations;
import scala.Tuple2;

public class SparkConsumerNetflix {
    private static final Logger logger = LogManager.getLogger(SparkConsumerNetflix.class);

    public static void main(final String... args) {
        // Configure connection to Kafka topic
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Commons.APP_KAFKA_SERVER);
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "SparkConsumerGroup");

        // Configure Spark to listen messages in defined topic using JavaStreamingContext
        Collection<String> topics = Arrays.asList(Commons.APP_KAFKA_TOPIC);
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("SparkConsumerApplication");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

        // Start reading messages from Kafka and get DStream
        final JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(jssc, LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams));
        
        // Read value of each message from Kafka and return it
        JavaDStream<String> lines = stream.map((Function<ConsumerRecord<String, String>, String>) kafkaRecord -> kafkaRecord.value());

        // Break every message into words and return list of ratings
        JavaDStream<String> ratings = lines.flatMap((FlatMapFunction<String, String>) line -> Arrays.asList(line.split(" ")).iterator());

        // Take every rating and return Tuple with (rating,1)
        JavaPairDStream<String, Integer> wordMap = ratings.mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1));

        // Count occurrence of each rating
        JavaPairDStream<String, Integer> wordCount = wordMap.reduceByKey((Function2<Integer, Integer, Integer>) (first, second) -> first + second);

        //Print the word count
        wordCount.print();

        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            logger.error("An error occurred.", e);
        }
    }
}
