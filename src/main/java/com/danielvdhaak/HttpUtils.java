package com.danielvdhaak;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpException;

/**
 * <p>
 * Utilities class containing util functions for making HTTP requests.
 * </p>
 * 
 * @see org.apache.commons.httpclient
 */
public class HttpUtils {
    private static final Logger logger = LogManager.getLogger(HttpUtils.class.getName());

    /**
     * Creates a client used to perform HTTP requests.
     * @param timeout HTTP call timeout in milliseconds, cannot be negative.
     * @return the HttpClient 
     */
    public static HttpClient createHttpClient(int timeout) {
        HttpClient client = new HttpClient();

        // Set connection timeout
        if (timeout < 0) {
            throw new IllegalArgumentException("Argument timeout cannot be negative");
        }

        client.getHttpConnectionManager().
            getParams().setConnectionTimeout(timeout);

        return client;
    }
}
