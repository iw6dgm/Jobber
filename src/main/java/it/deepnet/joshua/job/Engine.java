package it.deepnet.joshua.job;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class Engine {
    private String server = "";

    public void setServer(String s) {
        this.server = s;
    }

    public String getServer() {
        return this.server;
    }

    public Engine() {

        setServer(MySQL.server);

    }

    public boolean login(String user, String password) {

        boolean logged = false;
        ResultSet rs = null;
        CallableStatement cs = null;


        try (Connection connection = MySQL.open()) {

            try {

                cs = connection.prepareCall("{CALL login(?, ?)}");
                cs.setString(1, user);
                cs.setString(2, password);

                rs = cs.executeQuery();
                logged = rs.next();

            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }

        } catch (SQLException | IOException e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        }
        return logged;

    }

    public List<Project> getProjects(String user) {

        CallableStatement cs = null;
        ResultSet rs = null;
        ArrayList<Project> prj = null;
        Connection c = null;
        try {

            c = MySQL.open();

            try {

                cs = c.prepareCall("{CALL getProjects(?)}");

                cs.setString(1, user);
                rs = cs.executeQuery();

                prj = new ArrayList<Project>();

                while (rs.next()) {
                    prj.add(new Project(rs.getInt(1), rs.getString(2)));
                }

            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }

        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } finally {
            if (c != null) MySQL.close(c);
        }

        return prj;

    }

    public Status getStatus(String user_id) {

        CallableStatement cs = null;
        ResultSet rs = null;
        Connection c = null;
        Status status = new Status();

        try {

            c = MySQL.open();

            try {

                cs = c.prepareCall("{CALL getStatus(?)}");
                cs.setString(1, user_id);

                rs = cs.executeQuery();
                if (rs.next()) {

                    status.setStatus(rs.getInt(1));
                    status.setProject_id(rs.getInt(2));
                    status.setEvent_id(rs.getInt(3));

                }

            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }
        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } finally {
            if (c != null) MySQL.close(c);
        }

        return status;
    }

    public String getNote(int id) {

        String note = "<Empty>";
        Connection c = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {

            c = MySQL.open();

            try {

                cs = c.prepareCall("{CALL getNote(?)}");
                cs.setInt(1, id);

                rs = cs.executeQuery();

                if (rs.next()) {
                    note = rs.getString(1);
                }

            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }

        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } finally {
            if (c != null) MySQL.close(c);
        }

        return note;

    }

    public void updateNote(int id, String note) {

        CallableStatement cs = null;
        Connection c = null;
        try {

            c = MySQL.open();

            try {
                cs = c.prepareCall("{CALL updateNote(?, ?)}");
                cs.setInt(1, id);
                cs.setString(2, note);
                cs.execute();
            } finally {
                if (cs != null) cs.close();
            }

        } catch (SQLException e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } catch (Exception e2) {
            Job.logger.log(Level.SEVERE, "{Engine}", e2);
        } finally {
            if (c != null) MySQL.close(c);
        }
    }

    public int startProject(String user_id, int project_id) {

        int event_id = 0;
        ResultSet rs = null;
        Connection c = null;
        CallableStatement cs = null;

        Job.logger.log(Level.FINE, "call startProject('" + user_id +
                "'," + project_id + ")");

        try {

            c = MySQL.open();

            try {

                cs = c.prepareCall("{CALL startProject(?, ?)}");
                cs.setString(1, user_id);
                cs.setInt(2, project_id);

                rs = cs.executeQuery();

                if (rs.next()) {
                    event_id = rs.getInt(1);
                }
            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }
        } catch (SQLException e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } catch (Exception e2) {
            Job.logger.log(Level.SEVERE, "{Engine}", e2);
        } finally {
            if (c != null) MySQL.close(c);
        }

        return event_id;
    }

    public void stopProject(String user_id) {

        CallableStatement cs = null;
        Connection c = null;
        Job.logger.log(Level.FINE, "call stopProject('" + user_id + "')");

        try {

            c = MySQL.open();

            try {

                cs = c.prepareCall("{CALL stopProject(?)}");
                cs.setString(1, user_id);
                cs.execute();

            } finally {
                if (cs != null) cs.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } catch (Exception e2) {
            Job.logger.log(Level.SEVERE, "{Engine}", e2);
        } finally {
            if (c != null) MySQL.close(c);
        }

    }
}
