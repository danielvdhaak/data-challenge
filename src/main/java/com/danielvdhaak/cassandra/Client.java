package com.danielvdhaak.cassandra;

import java.util.*;

import com.datastax.driver.core.Session;

public class Client {
    public static void main(String args[]) {
        Connector connector = new Connector();

        connector.connect("localhost", 9042);

        Session session = connector.getSession();

        Keyspace keyspace = new Keyspace(session);
        keyspace.create("crypto", 1);
        keyspace.use("crypto");

        Table table = new Table(session);

        HashMap<String, String> columns = new HashMap<String, String>();
        columns.put("id", "bigint PRIMARY KEY");
        columns.put("time", "date");
        columns.put("price", "float");

        table.create("trades", columns, true);

        connector.close();
    }
}
