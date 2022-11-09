package com.danielvdhaak;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App 
{
    private static final Logger logger = LogManager.getLogger(App.class);
    public static void main(final String... args) 
    {
        String APP_GOAL = System.getenv("APP_GOAL") != null ? 
                System.getenv("APP_GOAL") : "producer";

        logger.info("Kafka Topic: {}", Commons.APP_KAFKA_TOPIC);
        logger.info("Kafka Server: {}", Commons.APP_KAFKA_SERVER);
        logger.info("Zookeeper Server: {}", Commons.APP_ZOOKEEPER_SERVER);
        logger.info("GOAL: {}", APP_GOAL);

        switch (APP_GOAL.toLowerCase()) {
            case "producer":
                KafkaProducerCrypto.main();
                break;
            case "consumer":
                SparkConsumerNetflix.main();
                break;
            default:
                logger.error("No valid goal to run.");
                break;
        }
    }
}
