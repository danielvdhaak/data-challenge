package com.danielvdhaak;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.*;

import static org.apache.spark.sql.types.DataTypes.*;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.explode;
import static org.apache.spark.sql.functions.from_json;


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

        // Define DataFrame schema
        StructType structSchema = new StructType()
            .add("id", LongType, false)
            .add("price", StringType, false)
            .add("qty", StringType, false)
            .add("quoteQty", StringType, false)
            .add("time", LongType, false)
            .add("isBuyerMaker", BooleanType, false)
            .add("isBestMatch", BooleanType, false);
        ArrayType schema = createArrayType(structSchema);

        // Read Kafka stream
        Dataset<Row> df = spark.readStream()
        .format("kafka")
        .option("kafka.bootstrap.servers", Commons.APP_KAFKA_SERVER)
        .option("subscribe", Commons.APP_KAFKA_TOPIC)
        .load();
        df = df.selectExpr("CAST(value AS STRING)");
        
        // Construct DataFrame from json data
        Dataset<Row> cryptoDF = df.withColumn("json", from_json(
                df.col("value"), 
                schema
            )
        )
        .withColumn("json", explode(col("json")))
        .select(col("json.*"));

        cryptoDF.printSchema();

        // Start query that prints df
        StreamingQuery query = cryptoDF.writeStream()
            .outputMode("append")
            .option("truncate", true)
            .format("console")
            .start();

        try {
            query.awaitTermination();
        } catch (StreamingQueryException e) {
            logger.error(e.getMessage());
        }
    }
}