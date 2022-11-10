package com.danielvdhaak;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

public class KafkaProducerCrypto {
    private static final Logger logger = LogManager.getLogger(KafkaProducerCrypto.class);

    public static void main(final String... args) 
    {
        // Declare API endpoint
        String url = "https://api.binance.com/api/v3/trades?symbol=BTCUSDT&limit=50";
        
        // Create singular HttpClient object
        HttpClient client = HttpUtils.createHttpClient(0);

        // Initialize producer class
        final Producer<String, String> producer = createProducer();
        int EXAMPLE_PRODUCER_INTERVAL = System.getenv("APP_PRODUCER_INTERVAL") != null ?
                Integer.parseInt(System.getenv("APP_PRODUCER_INTERVAL")) : 100;

        try {
            while (true) {
                long start = System.nanoTime();

                // Send GET request
                HttpMethod response = HttpUtils.sendGetRequest(client, url);
                String responseBody = HttpUtils.parseResponseBody(response);

                // Continue on empty string?

                // Publish producer record to Kafka topic
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    Commons.APP_KAFKA_TOPIC,
                    responseBody);
                RecordMetadata metadata = producer.send(record).get();

                logger.info("Sent record to topic {} @ {}.", metadata.topic(), metadata.timestamp());

                // Determine time to delay
                float timeElapsed = ((float)(System.nanoTime() - start))*1e-6f;
                int delay = EXAMPLE_PRODUCER_INTERVAL - (int)timeElapsed >= 0 ? 
                    EXAMPLE_PRODUCER_INTERVAL - (int)timeElapsed : 0;
                
                Thread.sleep(delay);
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