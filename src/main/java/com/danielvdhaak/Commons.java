package com.danielvdhaak;

/**
 * Loads string configurations from environment variables. 
 * Assumes default values if no environment variables are defined.
 */
public class Commons {
    public final static String APP_KAFKA_TOPIC = System.getenv("APP_KAFKA_TOPIC") != null ?
        System.getenv("APP_KAFKA_TOPIC") : "crypto";
    public final static String APP_KAFKA_SERVER = System.getenv("APP_KAFKA_SERVER") != null ?
        System.getenv("APP_KAFKA_SERVER") : "localhost:9094";
    public final static String APP_ZOOKEEPER_SERVER = System.getenv("APP_ZOOKEEPER_SERVER") != null ?
        System.getenv("APP_ZOOKEEPER_SERVER") : "localhost:2181";
}