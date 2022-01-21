package it.deepnet.joshua.job;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class Database {

    public static String server = "/Users/maurizio_camangi/db";
    public static String dbname = "jobber.db";
    public static String user = "test";

    static Connection open() throws IOException {

        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Job.logger.log(Level.SEVERE, Database.class.getSimpleName(), e);
        }
        //c = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + server + "/" + dbname);
            c.setAutoCommit(true);
        } catch (SQLException e1) {
            Job.logger.log(Level.SEVERE, Database.class.getSimpleName(), e1);
        }

        if (c != null) Job.logger.log(Level.FINE, "Connection ok!");
        else Job.logger.log(Level.SEVERE, Database.class.getSimpleName(), "Connection failed!");
        return c;
    }

    static void close(Connection c) {

        try {
            c.close();
        } catch (SQLException e2) {
            Job.logger.log(Level.SEVERE, Database.class.getSimpleName(), e2);
        }


        Job.logger.log(Level.FINE, "Connection closed!");
    }

}
