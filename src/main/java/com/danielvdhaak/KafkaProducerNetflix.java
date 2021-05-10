package com.danielvdhaak;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class KafkaProducerNetflix {
    private static final Logger logger = LogManager.getLogger(KafkaProducerNetflix.class);

    public static void main(final String... args) 
    {
        // Randomizer
        Random rnd = new Random(System.currentTimeMillis());

        // Initialize key counter
        int key = 1;

        // Initialize producer class
        final Producer<String, String> producer = createProducer();
        int EXAMPLE_PRODUCER_INTERVAL = System.getenv("APP_PRODUCER_INTERVAL") != null ?
                Integer.parseInt(System.getenv("APP_PRODUCER_INTERVAL")) : 100;

        try {
            while (true) {
                // Generate a random rating between 1 and 5
                int rating = rnd.nextInt(5) + 1;

                // Publish producer record to Kafka topic
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    Commons.APP_KAFKA_TOPIC, 
                    String.valueOf(key), 
                    String.valueOf(rating));
                RecordMetadata metadata = producer.send(record).get();

                logger.info("Sent ({}, {}) to topic {} @ {}.", key, rating, Commons.APP_KAFKA_TOPIC, metadata.timestamp());

                key++;

                Thread.sleep(EXAMPLE_PRODUCER_INTERVAL);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred.", e);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Commons.APP_KAFKA_SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerNetflix");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}
