package com.danielvdhaak;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaProducerNetflix {
    private static final Logger logger = LogManager.getLogger(KafkaProducerNetflix.class);

    public static void main(final String... args) 
    {
        // Produce random string of words
        String[] words = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        Random ran = new Random(System.currentTimeMillis());

        final Producer<String, String> producer = createProducer();

        // Get producer interval, otherwise parse default
        int EXAMPLE_PRODUCER_INTERVAL = System.getenv("APP_PRODUCER_INTERVAL") != null ?
                Integer.parseInt(System.getenv("APP_PRODUCER_INTERVAL")) : 100;

        try {
            while (true) {
                String word = words[ran.nextInt(words.length)];
                String uuid = UUID.randomUUID().toString();

                ProducerRecord<String, String> record = new ProducerRecord<>(Commons.APP_KAFKA_TOPIC, uuid, word);
                RecordMetadata metadata = producer.send(record).get();

                logger.info("Sent ({}, {}) to topic {} @ {}.", uuid, word, Commons.APP_KAFKA_TOPIC, metadata.timestamp());

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
