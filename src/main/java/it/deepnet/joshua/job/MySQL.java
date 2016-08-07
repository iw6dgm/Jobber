package it.deepnet.joshua.job;

import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQL {

    public static String server = "127.0.0.1";
    public static String dbname = "job";
    public static String user = "job";
    public static String password = "";
    public static String encrypt = "false";

    static Connection open() throws IOException {

        Connection c = null;
        String decodedPassword;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            Job.logger.log(Level.SEVERE, "{MySQL}", e);
        } catch (IllegalAccessException e) {
            Job.logger.log(Level.SEVERE, "{MySQL}", e);
        } catch (ClassNotFoundException e) {
            Job.logger.log(Level.SEVERE, "{MySQL}", e);
        }
        //c = null;
        try {
            decodedPassword = password;
            if ("true".equals(encrypt)) {
                BASE64Decoder base64Decoder = new BASE64Decoder();
                decodedPassword = new String(base64Decoder.decodeBuffer(new String(base64Decoder.decodeBuffer(password))));
            }
            c = DriverManager.getConnection("jdbc:mysql://" + server + "/" + dbname + "?user=" + user + "&password=" + decodedPassword);
            c.setAutoCommit(true);
        } catch (SQLException e1) {
            Job.logger.log(Level.SEVERE, "{MySQL}", e1);
        }

        if (c != null) Job.logger.log(Level.FINE, "Connection ok!");
        else Job.logger.log(Level.SEVERE, "{MySQL}", "Connection failed!");
        return c;
    }

    static void close(Connection c) {

        try {
            c.close();
        } catch (SQLException e2) {
            Job.logger.log(Level.SEVERE, "{MySQL}", e2);
        }


        Job.logger.log(Level.FINE, "Connection closed!");
    }

}
