package com.danielvdhaak.cassandra;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

public class Utils {
    private static final Logger logger = LogManager.getLogger(Utils.class);

    /**
     * Executes a CQL query.
     * 
     * @param session
     * @param query
     * @return
     */
    public static ResultSet executeQuery(Session session, String query) {
        ResultSet rs = session.execute(query);
        logger.debug("Cassandra - Successfully sent query");
        
        return rs;
    }


}
