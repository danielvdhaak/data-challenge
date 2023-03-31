package com.danielvdhaak.cassandra;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

public class Query {
    private static final Logger logger = LogManager.getLogger(Connector.class);

    private Session session;
    private String query;

    public Query(Session session, String query) {
        this.session = session;
        
    }


}
