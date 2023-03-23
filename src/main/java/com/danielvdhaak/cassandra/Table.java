package com.danielvdhaak.cassandra;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class Table {
    private static final Logger logger = LogManager.getLogger(Table.class);

    private Session session;

    public Table(Session session) {
        this.session = session;
    }

    /**
     * Creates a Cassandra Table.
     * 
     * @param name the name of the Table
     * @param columns the columns in format <name, type>, where PRIMARY KEY can be added to type
     * @param ifNotExists whether to override the error when the Table already exists
     */
    public void create(String name, HashMap<String, String> columns, boolean ifNotExists) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }

        // Add name and columns
        sb.append(name).append(" ( ");
        for (Map.Entry<String, String> entry: columns.entrySet()) {
            sb.append(entry.getKey())
                .append(" ")
                .append(entry.getValue())
                .append(",");
        }
        sb.append(" )");

        final String query = sb.toString();
        session.execute(query);
        logger.info("Cassandra - Created Table '" + name + "'");
    }

    /**
     * Creates a Cassandra Table.
     * 
     * @param name the name of the Table
     * @param keyspace the name of the Keyspace to add the Table to
     * @param columns the columns in format <name, type>, where PRIMARY KEY can be added to type
     * @param ifNotExists whether to override the error when the Table already exists
     */
    public void create(String name, String keyspace, HashMap<String, String> columns, boolean ifNotExists) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        if (ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }

        // Add keyspace, name and columns
        sb.append(keyspace).append(".").append(name).append(" ( ");
        for (Map.Entry<String, String> entry: columns.entrySet()) {
            sb.append(entry.getKey())
                .append(" ")
                .append(entry.getValue())
                .append(",");
        }
        sb.append(" )");

        final String query = sb.toString();
        session.execute(query);
        logger.info("Cassandra - Created Table '" + name + "'");
    }

    /**
     * Returns a list of all Tables in the cluster.
     * 
     * @param keyspace the name of the Keyspace that the Table resides in
     * 
     * @return String list of all Tables in the cluster
     */
    public List<String> list(String keyspace) {
        StringBuilder sb = new StringBuilder("SELECT * FROM system_schema.tables WHERE keyspace_name = '")
            .append(keyspace)
            .append("';");
        final String query = sb.toString();

        ResultSet rs = session.execute(query);

        List<String> tables = new ArrayList<String>();
        for (Row r : rs) {
            tables.add(r.getString("table_name"));
        }

        return tables;
    }

    /**
     * Deletes a specified Cassandra Table. Results in the immediate removal of the Table, 
     * includnig all data contained in the Table.
     * 
     * @param table the name of the Table to delete
     */
    public void delete(String table) {
        StringBuilder sb = new StringBuilder("DROP TABLE ")
            .append(table);
        
        final String query = sb.toString();
        session.execute(query);
        logger.info("Cassandra - Deleted Table '" + table + "'");
    }
}