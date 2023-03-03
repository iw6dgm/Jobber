package it.deepnet.joshua.job;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    
    private static final Logger logger = Logger.getLogger(Database.class.getSimpleName());

    public static String server = "/Users/maurizio_camangi/db";
    public static String dbname = "jobber.db";

    static Connection open(final boolean autocommit) throws Exception {
        Connection c = null;
        SQLiteJDBCLoader.initialize();
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + server + "/" + dbname);
        try {
            c = dataSource.getConnection();
            c.setAutoCommit(autocommit);
        } catch (SQLException e1) {
            logger.log(Level.SEVERE, Database.class.getSimpleName(), e1);
        }

        if (c != null) logger.log(Level.FINE, "Connection ok!");
        else logger.log(Level.SEVERE, Database.class.getSimpleName(), "Connection failed!");
        return c;
    }

    static void close(Connection c) {

        try {
            c.close();
        } catch (SQLException e2) {
            logger.log(Level.SEVERE, Database.class.getSimpleName(), e2);
        }


        logger.log(Level.FINE, "Connection closed!");
    }

}
