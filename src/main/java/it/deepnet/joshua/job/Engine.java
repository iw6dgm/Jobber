package it.deepnet.joshua.job;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Engine {
    private static final Logger logger = Logger.getLogger(Engine.class.getSimpleName());
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
            logger.log(Level.SEVERE, "{Engine}", e);
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
            logger.log(Level.SEVERE, "{Engine}", e);
        }

        return prj;

    }

    public Status getStatus(String user_id) {

        try (final Connection c = Database.open(true)) {

            try (final PreparedStatement ps = c.prepareStatement("SELECT e.project_id, e.rowid AS event_id FROM user_event u JOIN event_store e ON u.event_id = e.rowid WHERE u.user_id=?")) {

                ps.setString(1, user_id);

                try (final ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {

                        return new Status(rs.getString("project_id"), rs.getInt("event_id"));

                    }
                }

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{Engine}", e);
        }

        return INACTIVE_STATUS;
    }

    public String getNote(int id) {

        try {

            try (final Connection c = Database.open(true)) {

                try (final PreparedStatement ps = c.prepareStatement("SELECT body FROM event_store WHERE parent_id = ? AND event_type = ? ORDER BY event_date DESC LIMIT 1")) {
                    ps.setInt(1, id);
                    ps.setString(2, "NOTE");
                    try (final ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{Engine}", e);
        }

        return "<Empty>";
    }

    public void updateNote(String user_id, String project_id, int event_id, String note) {
        try (final Connection c = Database.open(true)) {

            try (final PreparedStatement ps = c.prepareStatement("INSERT INTO event_store (user_id,project_id,event_type,parent_id,body) VALUES(?,?,?,?,?)")) {
                ps.setString(1, user_id);
                ps.setString(2, project_id);
                ps.setString(3, "NOTE");
                ps.setInt(4, event_id);
                ps.setString(5, note);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "{Engine}", e);
        }
    }

    public int startProject(String user_id, String project_id) {

        logger.log(Level.FINE, "call startProject('" + user_id +
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
                            try (final PreparedStatement ps2 = c.prepareStatement("INSERT INTO user_event(user_id, event_id) VALUES(?,?) ON CONFLICT(user_id) DO UPDATE SET event_id=?")) {
                                ps2.setString(1, user_id);
                                ps2.setInt(2, event_id);
                                ps2.setInt(3, event_id);

                                if (ps2.executeUpdate() > 0) {
                                    c.commit();
                                    return event_id;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{Engine}", e);
        }

        return -1;
    }

    public boolean stopProject(String user_id) {

        logger.log(Level.FINE, "call stopProject('" + user_id + "')");

        try (final Connection c = Database.open(false)) {

            try (final PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO event_store(user_id,project_id,event_type,parent_id)\n" +
                            "SELECT u.user_id, e.project_id, ? as event_type, u.event_id\n" +
                            "FROM user_event u JOIN event_store e ON u.event_id = e.rowid WHERE u.user_id=?")) {

                ps.setString(1, "PROJECT_STOP");
                ps.setString(2, user_id);
                if (ps.executeUpdate() > 0) {

                    try (final PreparedStatement ps2 = c.prepareStatement("UPDATE user_event SET event_id=NULL WHERE user_id=?")) {
                        ps2.setString(1, user_id);
                        if (ps2.executeUpdate() > 0) {
                            c.commit();
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{Engine}", e);
        }
        return false;
    }
}
