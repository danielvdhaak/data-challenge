package com.danielvdhaak;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.apache.commons.httpclient.HttpClient;


public class HttpUtilsTest {
    
    @Test
    public void createSimpleHttpClient() {
        Object httpClient = HttpUtils.createHttpClient(0);

        assertThat(httpClient, instanceOf(HttpClient.class));
    }

    @Test
    public void testHttpClientTimeout() {
        HttpClient httpClient = HttpUtils.createHttpClient(100);

        assertEquals(httpClient.getHttpConnectionManager().getParams().getConnectionTimeout() ,100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHttpClientNegativeTimeout() {
        HttpClient httpClient = HttpUtils.createHttpClient(-100);
    }
}
