package com.danielvdhaak.cassandra;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class Keyspace {
    private static final Logger logger = LogManager.getLogger(Keyspace.class);

    private Session session;
    private String currentKeyspace;

    public Keyspace(Session session) {
        this.session = session;
    }

    /**
     * Creates a Cassandra Keyspace, provided it does not exist yet.
     * 
     * @param keyspace the name of the Keyspace
     * @param replicas the replication factor applied to the entire cluster
     */
    public void create(String name, int replicas) {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
            .append(name)
            .append(" WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : ")
            .append(replicas)
            .append("};");

        final String query = sb.toString();
        logger.info("Cassandra - Created Keyspace '" + name + "'");
    }

    /**
     * Identifies a Cassandra Keyspace for the current client session. 
     * All subsequent operations on tables and indexes are in the context of the named Keyspace, 
     * unless otherwise specified or until the client connection is terminated or another USE statement is issued.
     * 
     * @param keyspace the name of the Keyspace to use
     */
    public void use(String keyspace) {
        session.execute("USE " + keyspace);
        this.currentKeyspace = keyspace;
        logger.info("Cassandra - Using Keyspace '" + keyspace + "' for current session");
    }

    /**
     * Returns a list of all Keyspaces in the cluster.
     * 
     * @return String list of all Keyspaces in the cluster
     */
    public List<String> list() {
        ResultSet rs = session.execute("SELECT * FROM system_schema.keyspaces;");

        List<String> keyspaces = new ArrayList<String>();
        for (Row r : rs) {
            keyspaces.add(r.getString("keyspace_name"));
        }

        return keyspaces;
    }

    /**
     * Delete a specified Cassandra Keyspace. Results in the immediate removal of the Keyspace, 
     * includnig all tables and data contained in the Keyspace.
     * 
     * @param keyspace the name of the Keyspace to delete
     */
    public void delete(String keyspace) {
        StringBuilder sb = new StringBuilder("DROP KEYSPACE ")
            .append(keyspace);

        final String query = sb.toString();

        session.execute(query);
        logger.info("Cassandra - Deleted Keyspace '" + keyspace + "'");
    }

    /**
     * Returns the currently used Keyspace, following the USE command.
     * 
     * @return the current Keyspace
     */
    public String getCurrent() {
        return this.currentKeyspace;
    }
}
