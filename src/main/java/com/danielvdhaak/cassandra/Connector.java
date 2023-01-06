package com.danielvdhaak.cassandra;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;


public class Connector {
    private static final Logger logger = LogManager.getLogger(Connector.class);

    private Cluster cluster;
    private Session session;
    private Set<Host> hosts;

    public void connect(String node, Integer port) {
        Builder builder = Cluster.builder().addContactPoint(node);
        if (port != null) {
            builder.withPort(port);
        }
        cluster = builder.build();

        // Start connection
        session = cluster.connect();
        logger.info(
            "Successfully found Cassandra cluster " + cluster.getClusterName() 
            + " via contact point " + node + ":" + (port != null ? port : "9042")
        );
        
        // Retrieve addresses of hosts (e.g. cluster nodes)
        Metadata metadata = cluster.getMetadata();
        hosts = metadata.getAllHosts();
    }

    public Session getSession() {
        return this.session;
    }

    public Set<Host> getHosts() {
        return this.hosts;
    }

    public void close() {
        session.close();
        cluster.close();
        logger.info("Connection to Cassandra cluster successfully closed!");
    }
}
