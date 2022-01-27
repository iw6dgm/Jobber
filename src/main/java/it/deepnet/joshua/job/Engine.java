package it.deepnet.joshua.job;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class Engine {
    private static final Status INACTIVE_STATUS = new Status();
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
        try (final Connection connection = Database.open(true)) {

            try (final PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM login WHERE user=? AND password=?")) {

                ps.setString(1, user);
                ps.setString(2, password);

                try (final ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException | IOException e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        }
        return logged;

    }

    public List<Project> getProjects(String user) {

        final List<Project> prj = new ArrayList<>();

        try (final Connection c = Database.open(true)) {
            try (final PreparedStatement ps = c.prepareStatement("SELECT p.id, p.description FROM project p JOIN user_project up ON (p.id=up.project_id) WHERE up.user_id=?")) {
                ps.setString(1, user);
                try (final ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        prj.add(new Project(rs.getString(1), rs.getString(2)));
                    }
                }
            }
        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        }

        return prj;

    }

    public Status getStatus(String user_id) {

        try (final Connection c = Database.open(true)) {

            try (final PreparedStatement ps = c.prepareStatement("SELECT project_id, event_id FROM user_event WHERE user_id=?")) {

                ps.setString(1, user_id);

                try (final ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {

                        return new Status(rs.getString("project_id"), rs.getInt("event_id"));

                    }
                }

            }
        } catch (Exception e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        }

        return INACTIVE_STATUS;
    }

    public String getNote(int id) {

        String note = "<Empty>";
        Connection c = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {

            c = Database.open(true);

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

            c = Database.open(false);

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

        Job.logger.log(Level.FINE, "call startProject('" + user_id +
                "'," + project_id + ")");

        try (final Connection c = Database.open(false)) {
            try (final PreparedStatement ps = c.prepareStatement("INSERT INTO event_store(user_id,project_id,event_type) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, user_id);
                ps.setString(2, project_id);
                ps.setString(3, "PROJECT_START");

                if (ps.executeUpdate() > 0) {
                    try (final ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            final int event_id = rs.getInt(1);
                            try (final PreparedStatement ps2 = c.prepareStatement("UPDATE user_event SET event_id=?,project_id=? WHERE user_id=?")) {
                                ps2.setInt(1, event_id);
                                ps2.setString(2, project_id);
                                ps2.setString(3, user_id);

                                if (ps2.executeUpdate()>0) {
                                    c.commit();
                                    return event_id;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } catch (Exception e2) {
            Job.logger.log(Level.SEVERE, "{Engine}", e2);
        }

        return -1;
    }

    public boolean stopProject(String user_id, String user_project_id) {

        Job.logger.log(Level.FINE, "call stopProject('" + user_id + "')");

        try (final Connection c = Database.open(false)) {

            try (final PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO event_store(user_id,project_id,event_type,parent_id)\n" +
                    "SELECT user_id, project_id, ? as event_type, event_id\n" +
                    "FROM user_event where user_id=? AND project_id=?")) {

                ps.setString(1, "PROJECT_STOP");
                ps.setString(2, user_id);
                ps.setString(3, user_project_id);
                if (ps.executeUpdate()>0) {
                    c.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Job.logger.log(Level.SEVERE, "{Engine}", e);
        } catch (Exception e2) {
            Job.logger.log(Level.SEVERE, "{Engine}", e2);
        }
        return false;
    }
}
