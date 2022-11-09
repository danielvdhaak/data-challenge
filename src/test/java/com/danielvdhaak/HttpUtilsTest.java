package com.danielvdhaak;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;


public class HttpUtilsTest {

    @Test
    public void testHttpClientTimeout() {
        HttpClient httpClient = HttpUtils.createHttpClient(100);

        assertEquals(httpClient.getHttpConnectionManager().getParams().getConnectionTimeout() ,100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHttpClientNegativeTimeout() {
        HttpClient httpClient = HttpUtils.createHttpClient(-100);
    }

    @Test
    public void testRequestUrl() throws URIException {
        String url = "https://api.binance.com/api/v3/ping";
        HttpMethod request = HttpUtils.createGetRequest(url);

        assertEquals(request.getURI().toString(), url);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadRequestUrl() {
        HttpMethod httpMethod = HttpUtils.createGetRequest("Not a URL");
    }



}
