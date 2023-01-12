package com.danielvdhaak.cassandra;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Session;

public class Keyspace {
    private static final Logger logger = LogManager.getLogger(Connector.class);

    private Session session;

    public Keyspace(Session session) {
        this.session = session;
    }

    /**
     * Creates a Cassandra Keyspace, provided it does not exist yet.
     * 
     * @param keyspace the name of the Keyspace
     * @param replicas the replication factor applied to the entire cluster
     */
    public void create(String keyspace, int replicas) {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
            .append(keyspace)
            .append(" WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : ")
            .append(replicas)
            .append("};");

        final String query = sb.toString();
        logger.info("Cassandra - Created Keyspace '" + keyspace + "'");
    }

    /**
     * Identifies a Cassandra Keyspace for the current client session. 
     * All subsequent operations on tables and indexes are in the context of the named Keyspace, 
     * unless otherwise specified or until the client connection is terminated or another USE statement is issued.
     * 
     * @param keyspace the name of the Keyspace to use.
     */
    public void use(String keyspace) {
        session.execute("USE " + keyspace);
        logger.info("Cassandra - Using Keyspace '" + keyspace + "' for current session");
    }

    /**
     * Delete a specified Cassandra Keyspace. Results in the immediate removal of the Keyspace, 
     * includnig all tables and data contained in the Keyspace.
     * 
     * @param keyspace the name of the Keyspace to delete.
     */
    public void delete(String keyspace) {
        StringBuilder sb = new StringBuilder("DROP KEYSPACE ")
            .append(keyspace);

        final String query = sb.toString();

        session.execute(query);
        logger.info("Cassandra - Deleted Keyspace '" + keyspace + "'");
    }
}
