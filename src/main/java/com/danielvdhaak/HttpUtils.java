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

    /**
     * Creates a HttpMethod object used to perform GET requests.
     * @param url URL to send HTTP request to
     * @return the request object to be executed
     */
    public static HttpMethod createGetRequest(String url) {
        // TODO: Implement other HTTP methods
        HttpMethod request = new GetMethod(url);
        request.setFollowRedirects(false);

        return request;
    }

    /**
     * Performs an HTTP GET request to a given URL.
     * @param client A HttpClient object
     * @see createHttpCient()
     * @param url The URL to send the request to
     * @return The HTTP response
     */
    public static HttpMethod sendGetRequest(HttpClient client, String url) {
        // Create request method
        HttpMethod request = createGetRequest(url);

        // Retrieve response (this populates the request object)
        try {
            client.executeMethod(request);

            String status = request.getStatusText();
            int code = request.getStatusCode();

            logger.debug("[{}] {} ({})", code, status, url);
        } catch (HttpException he) {
            logger.error("HTTP error connecting to '" + url + "'");
            logger.error(he.getMessage());
            System.exit(-4);
        } catch (IOException ioe) {
            logger.error("Unable to connect to '" + url + "'");
            System.exit(-3);
        }

        return request;
    }

    /**
     * Parses the HTTP response body as a string. This does not handle empty responses.
     * @param response HttpMethod object that has been executed
     * @return HTTP response body in string format (can be empty/null)
     */
    public static String parseResponseBody(HttpMethod response) {
        // Parse response body as string
        String body = null;
        try {
            body = response.getResponseBodyAsString();
        } catch (IOException e) {
            logger.error("Error parsing response body: " + e.getMessage());
        }

        return body;
    }
}