package com.danielvdhaak;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;


public class HttpUtilsTest {

    private static final String API_PING_ENDPOINT = "https://api.binance.com/api/v3/ping";

    @Test
    public void testHttpClientTimeout() {
        HttpClient httpClient = HttpUtils.createHttpClient(100);

        assertEquals(100, httpClient.getHttpConnectionManager().getParams().getConnectionTimeout());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHttpClientNegativeTimeout() {
        HttpClient httpClient = HttpUtils.createHttpClient(-100);
    }

    @Test
    public void testRequestUrl() throws URIException {
        String url = API_PING_ENDPOINT;
        HttpMethod request = HttpUtils.createGetRequest(url);

        assertEquals(url, request.getURI().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadRequestUrl() {
        HttpMethod httpMethod = HttpUtils.createGetRequest("Not a URL");
    }

    @Test
    public void testGetRequest() throws IOException {
        HttpClient httpClient = HttpUtils.createHttpClient(0);

        HttpMethod response = HttpUtils.sendGetRequest(
            httpClient, 
            API_PING_ENDPOINT
        );

        assertEquals(200, response.getStatusCode());
        assertTrue(response.hasBeenUsed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadGetRequest() {
        HttpClient httpClient = HttpUtils.createHttpClient(0);

        HttpMethod response = HttpUtils.sendGetRequest(
            httpClient, 
            "Not a URL"
        );
    }

    @Test
    public void testResponseBodyParse() {
        HttpClient httpClient = HttpUtils.createHttpClient(0);

        HttpMethod response = HttpUtils.sendGetRequest(
            httpClient, 
            API_PING_ENDPOINT
        );

        String body = HttpUtils.parseResponseBody(response);
        
        assertEquals("{}", body);
    }

}