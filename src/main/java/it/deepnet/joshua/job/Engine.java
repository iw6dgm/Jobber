package it.deepnet.joshua.job;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

        setServer(Database.server);

    }

    public boolean login(String user, String password) {

        boolean logged = false;
        ResultSet rs = null;

        try (Connection connection = Database.open()) {

            try (PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM login WHERE user=? AND password=?")){

                ps.setString(1, user);
                ps.setString(2, password);

                rs = ps.executeQuery();
                logged = rs.next();

            } finally {
                if (rs != null) rs.close();
            }

        } catch (SQLException | IOException e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        }
        return logged;

    }

    public List<Project> getProjects(String user) {

        ResultSet rs = null;
        ArrayList<Project> prj = null;

        try (final Connection c = Database.open()){
            try (final PreparedStatement ps = c.prepareStatement("SELECT p.id, p.description FROM project p JOIN user_project up ON (p.id=up.project_id) WHERE up.user_id=?")) {


                ps.setString(1, user);
                rs = ps.executeQuery();

                prj = new ArrayList<>();

                while (rs.next()) {
                    prj.add(new Project(rs.getString(1), rs.getString(2)));
                }

            } finally {
                if (rs != null) rs.close();
            }

        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        }

        return prj;

    }

    public Status getStatus(String user_id) {

        CallableStatement cs = null;
        ResultSet rs = null;
        Connection c = null;
        Status status = new Status();

        try {

            c = Database.open();

            try {

                cs = c.prepareCall("{CALL getStatus(?)}");
                cs.setString(1, user_id);

                rs = cs.executeQuery();
                if (rs.next()) {

                    status.setStatus(rs.getInt(1));
                    status.setProject_id(rs.getString(2));
                    status.setEvent_id(rs.getInt(3));

                }

            } finally {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
            }
        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } finally {
            if (c != null) Database.close(c);
        }

        return status;
    }

    public String getNote(int id) {

        String note = "<Empty>";
        Connection c = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {

            c = Database.open();

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
            if (c != null) Database.close(c);
        }

        return note;

    }

    public void updateNote(int id, String note) {

        CallableStatement cs = null;
        Connection c = null;
        try {

            c = Database.open();

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
            if (c != null) Database.close(c);
        }
    }

    public int startProject(String user_id, String project_id) {

        int event_id = 0;
        ResultSet rs = null;
        Connection c = null;
        CallableStatement cs = null;

        Job.logger.log(Level.FINE, "call startProject('" + user_id +
                "'," + project_id + ")");

        try {

            c = Database.open();

            try {

                cs = c.prepareCall("{CALL startProject(?, ?)}");
                cs.setString(1, user_id);
                cs.setString(2, project_id);

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
            if (c != null) Database.close(c);
        }

        return event_id;
    }

    public void stopProject(String user_id) {

        CallableStatement cs = null;
        Connection c = null;
        Job.logger.log(Level.FINE, "call stopProject('" + user_id + "')");

        try {

            c = Database.open();

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
            if (c != null) Database.close(c);
        }

    }
}
