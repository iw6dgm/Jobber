package it.deepnet.joshua.job;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

public class Loadxml {
    private static Map<String, String> configuration = null;

    public static void init() {
        String os = System.getenv("OS");
        String home;
        if (os != null && os.contains("Windows")) home = System.getenv("HOMEPATH");
        else home = System.getenv("HOME");
        String paramfile = home + "/.job/jobconf.xml";
        //BasicConfigurator.configure();

        configuration = new HashMap<>();

        //Istanzia la factory del parser XML
        try {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(paramfile);
            prop.loadFromXML(fis);

            if (prop.getProperty("server") != null && !prop.getProperty("server").isEmpty()) {

                configuration.put("server", prop.getProperty("server"));
            } else configuration.put("server", "127.0.0.1");

            if (prop.getProperty("dbname") != null && !prop.getProperty("dbname").isEmpty()) {

                configuration.put("dbname", prop.getProperty("dbname"));
            } else configuration.put("dbname", "job");

            if (prop.getProperty("dbuser") != null && !prop.getProperty("dbuser").isEmpty()) {

                configuration.put("dbuser", prop.getProperty("dbuser"));
            } else configuration.put("dbuser", "job");

            if (prop.getProperty("dbpassword") != null && !prop.getProperty("dbpassword").isEmpty()) {

                configuration.put("dbpassword", prop.getProperty("dbpassword"));
            } else configuration.put("dbpassword", "");

            if (prop.getProperty("user") != null && !prop.getProperty("user").isEmpty()) {

                configuration.put("user", prop.getProperty("user"));
            } else configuration.put("user", "job");


            if (prop.getProperty("password") != null && !prop.getProperty("password").isEmpty()) {

                configuration.put("password", prop.getProperty("password"));
            } else configuration.put("password", "job");

            if (prop.getProperty("connection") != null && !prop.getProperty("connection").isEmpty()) {

                configuration.put("connection", prop.getProperty("connection"));
            } else configuration.put("connection", "mysql");

            if (prop.getProperty("httpserver") != null && !prop.getProperty("httpserver").isEmpty()) {

                configuration.put("httpserver", prop.getProperty("httpserver"));
            } else configuration.put("httpserver", "http://localhost");

            if (prop.getProperty("encrypt") != null && !prop.getProperty("encrypt").isEmpty()) {

                configuration.put("encrypt", prop.getProperty("encrypt"));
            } else configuration.put("encrypt", "false");

        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Loadxml}", e);
        }
    }

    public static String getValue(String key) {

        String value = null;
        value = configuration.get(key);
        if (value == null) value = "";
        //System.out.println(key + " is " + value);

        return value;

    }

}
