package it.deepnet.joshua.job;

import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    
    private static final Logger logger = Logger.getLogger(Database.class.getSimpleName());

    public final static String server = "%s/db";
    public static String dbname = "jobber.db";

    static Connection open(final boolean autocommit) throws Exception {
        Connection c = null;
        SQLiteJDBCLoader.initialize();
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        final String dbname = String.format(server, Job.HOME) + "/" + Database.dbname;
        dataSource.setUrl("jdbc:sqlite:" + dbname);
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
