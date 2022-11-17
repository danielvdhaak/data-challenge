package com.danielvdhaak;

import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.TimestampType;
import org.apache.spark.sql.types.BooleanType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.FloatType;
import org.apache.spark.sql.types.IntegerType;

public class SparkConsumerCrypto {
    private static final Logger logger = LogManager.getLogger(SparkConsumerCrypto.class);

    public static void main(final String... args) {
        // Initialize Spark  
        SparkSession spark = SparkSession
        .builder()
        .appName("SparkCrypto")
        .config("spark.master", "local[2]")
        .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        // Read Kafka stream
        Dataset<Row> df = spark.readStream()
        .format("kafka")
        .option("kafka.bootstrap.servers", Commons.APP_KAFKA_SERVER)
        .option("subscribe", Commons.APP_KAFKA_TOPIC)
        .load();
        df = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");
        
        // Declare DataFrame schema
        StructType schema = new StructType()
            .add("id", new IntegerType(), false)
            .add("price", new FloatType(), false)
            .add("qty", new FloatType(), false)
            .add("quoteQty", new FloatType(), false)
            .add("time", new TimestampType(), false)
            .add("isBuyerMaker", new BooleanType(), false)
            .add("isBestMatch",  new BooleanType(), false);
        
        Dataset<Row> cryptoDF = df.select(
                functions.from_json(
                    df.col("value"), DataType.fromJson(schema.json())
                ).as("data")
            ).select("data.*");

        // TODO: Fix null values in table

        // Start query that prints df
        StreamingQuery query = cryptoDF.writeStream()
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