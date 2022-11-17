package com.danielvdhaak;

import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class SparkConsumerCrypto {
    private static final Logger logger = LogManager.getLogger(SparkConsumerCrypto.class);

    public static void main(final String... args) {
        SparkSession spark = SparkSession
        .builder()
        .appName("SparkCrypto")
        .config("spark.master", "local[2]")
        .getOrCreate();

        Dataset<Row> df = spark
            .readStream()
            .format("kafka")
            .option("kafka.bootstrap.servers", Commons.APP_KAFKA_SERVER)
            .option("subscribe", Commons.APP_KAFKA_TOPIC)
            .load();
        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

        // Start query that prints df
        StreamingQuery query = df.writeStream()
            .outputMode("append")
            .format("console")
            .start();

        try {
            query.awaitTermination();
        } catch (StreamingQueryException e) {
            logger.error(e.getMessage());
        }
    }
}