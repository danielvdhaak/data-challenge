package com.danielvdhaak.cassandra;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;


public class Connector {
    private static final Logger logger = LogManager.getLogger(Connector.class);

    private Cluster cluster;
    private Session session;
    private Set<Host> hosts;

    /**
     * Connects to a Cassandra cluster via a provided node and port.
     * 
     * @param node the Cassandra node host address to connect to (this can be any accessible node in the cluster)
     * @param port the port to use to connect to the Cassandra host
     * @throws NoHostAvailableException if the provided cluster host is not initialized yet or does not exist
     */
    public void connect(String node, Integer port) throws NoHostAvailableException {
        Builder builder = Cluster.builder().addContactPoint(node);
        if (port != null) {
            builder.withPort(port);
        }
        cluster = builder.build();

        // Start connection
        session = cluster.connect();
        logger.info(
            "Cassandra - Successfully found cluster '" + cluster.getClusterName() 
            + "' via contact point " + node + ":" + (port != null ? port : "9042")
        );
        
        // Retrieve addresses of hosts (e.g. cluster nodes)
        Metadata metadata = cluster.getMetadata();
        hosts = metadata.getAllHosts();
    }

    /**
     * Returns the current session.
     * 
     * @return the current session
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Returns all known cluster hosts.
     * 
     * @return all known cluster hosts
     */
    public Set<Host> getHosts() {
        return this.hosts;
    }

    /**
     * Closes the current session.
     */
    public void close() {
        session.close();
        cluster.close();
        logger.info("Cassandra - Connection to cluster successfully closed");
    }
}
