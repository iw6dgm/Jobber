/**
 * @version 0.85.3
 * @author Maurizio Camangi
 */
package it.deepnet.joshua.job;

import sun.misc.BASE64Decoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Job extends JFrame {

    static public final Logger logger = Logger.getLogger(Job.class.getName());
    private static final long serialVersionUID = 1L;
    private final String VERSION = "0.85.3 \t(C) Maurizio Camangi";
    Container cp;
    Status usr_status = null;
    Project usr_prj = null;

    //private static boolean isHTTPConnection = false;
    static Engine engine;
    boolean logged = false;
    int user_project_id = 1;
    int user_event_id = 0;
    static String user_id = "";
    static String user_pwd = "";
    List<Project> projects;
    JPanel title = new JPanel(),
            row1 = new JPanel(),
            row2 = new JPanel(),
            row3 = new JPanel(),
            row4 = new JPanel(),
            row5 = new JPanel();
    static JTextField username = new JTextField(12);
    static JPasswordField password = new JPasswordField(12);
    JButton jblogin = new JButton("Login"),
            jbcancel = new JButton("Cancel"),
            jbstart = new JButton("Start"),
            jbstop = new JButton("Stop"),
            jblogout = new JButton("Logout"),
            jbnote = new JButton("Note");
    JComboBox jcombo = new JComboBox();
    JTextArea status = new JTextArea(8, 35);
    ActionListener alogin = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            user_id = username.getText();
            user_pwd = new String(password.getPassword());
            doLogin();
        }
    },
            acancel = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    username.setText("");
                    password.setText("");
                }
            },
            astart = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    jbstart.setEnabled(false);
                    jbstop.setEnabled(true);
                    jbnote.setEnabled(true);
                    user_project_id = ((Project) jcombo.getSelectedItem()).getKey();
                    user_event_id = engine.startProject(user_id, user_project_id);

                    if (user_event_id != -1) {
                        status.append("Starting job name " +
                                jcombo.getSelectedItem().toString() + " with event_id=" +
                                user_event_id + "\n");
                        logger.log(Level.FINE, "Starting job name " +
                                jcombo.getSelectedItem().toString() + " with event_id=" +
                                user_event_id);
                        usr_prj = new Project(user_project_id, jcombo.getSelectedItem().toString());
                        jcombo.setEnabled(false);
                    } else {
                        status.append("WARNING : UNABLE TO START A PROJECT");
                        logger.warning("UNABLE TO START A PROJECT");
                    }

                }
            },
            astop = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    jbstart.setEnabled(true);
                    jbstop.setEnabled(false);
                    jbnote.setEnabled(false);
                    status.append("Stopping job name " +
                            usr_prj.getDescription() + "\n");
                    logger.log(Level.FINE, "Stopping job name " +
                            usr_prj.getDescription());
                    engine.stopProject(user_id);
                    jcombo.setEnabled(true);
                }
            },
            alogout = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    jbstart.setEnabled(false);
                    jbstop.setEnabled(false);
                    jbnote.setEnabled(false);
                    status.append("Logout " +
                            user_id + "...\n");
                    logger.log(Level.FINE, "Logout " +
                            user_id);
                    jcombo.setEnabled(false);
                    username.setEnabled(true);
                    password.setEnabled(true);
                    username.setText("");
                    password.setText("");
                    jblogin.setEnabled(true);
                    jbcancel.setEnabled(true);
                    jblogout.setEnabled(false);
                    status.append("Connection closed...\n");
                    logged = false;
                }
            },
            anote = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame frame = new Note(user_event_id);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(365, 205);
                    frame.setVisible(true);
                    frame.setResizable(false);
                }
            };

    public Job() {

        jblogin.addActionListener(alogin);
        jbcancel.addActionListener(acancel);
        jbstart.addActionListener(astart);
        jbstop.addActionListener(astop);
        jblogout.addActionListener(alogout);
        jbnote.addActionListener(anote);

        cp = getContentPane();
        cp.setLayout(new GridLayout(3, 1));

        title.setLayout(new FlowLayout(FlowLayout.CENTER));
        title.add(new JLabel("Jobber ver." + VERSION));

        row1.setLayout(new GridLayout(3, 1));
        row1.add(title);
        final JPanel usernameEntry = new JPanel();
        usernameEntry.setLayout(new FlowLayout(FlowLayout.CENTER));
        usernameEntry.add(new JLabel("Username"));
        usernameEntry.add(username);
        row1.add(usernameEntry);
        final JPanel passwordEntry = new JPanel();
        passwordEntry.setLayout(new FlowLayout(FlowLayout.CENTER));
        passwordEntry.add(new JLabel("Password"));
        passwordEntry.add(password);
        row1.add(passwordEntry);
        cp.add(row1);

        row2.setLayout(new GridLayout(2, 1));
        final JPanel loginButtons = new JPanel();
        loginButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        loginButtons.add(jblogin);
        loginButtons.add(jbcancel);
        row2.add(loginButtons);

        jcombo.setEnabled(false);
        final JPanel actionButtons = new JPanel();
        actionButtons.setLayout(new GridLayout(2, 1));
        row3.setLayout(new FlowLayout(FlowLayout.CENTER));
        row3.add(jcombo);
        actionButtons.add(row3);

        jbstart.setEnabled(false);
        jbstop.setEnabled(false);
        jblogout.setEnabled(false);
        jbnote.setEnabled(false);
        row5.setLayout(new FlowLayout(FlowLayout.CENTER));
        row5.add(jbstart);
        row5.add(jbstop);
        row5.add(jblogout);
        row5.add(jbnote);
        actionButtons.add(row5);
        row2.add(actionButtons);
        cp.add(row2);

        status.setEditable(false);

        row4.setLayout(new FlowLayout(FlowLayout.CENTER));
        row4.add(new JScrollPane(status));
        cp.add(row4);

        status.setWrapStyleWord(true);
        status.setLineWrap(true);
        status.append("Server is " + engine.getServer() + "\n");

        if ((user_id + user_pwd).length() > 0) {
            doLogin();
        }
    }

    public static void main(final String[] args) throws IOException {

        //Job.logger.setLevel(Level.INFO);

        Loadxml.init();

        if (Loadxml.getValue("server") != null) {
            MySQL.server = Loadxml.getValue("server").trim();
        } else {
            MySQL.server = "localhost";
        }

        logger.log(Level.CONFIG, "MySQL server = " + MySQL.server);

        if (Loadxml.getValue("dbuser") != null) {
            MySQL.user = Loadxml.getValue("dbuser").trim();
        } else {
            MySQL.user = "job";
        }

        logger.log(Level.CONFIG, "MySQL dbuser = " + MySQL.user);


        if (Loadxml.getValue("dbpassword") != null) {
            MySQL.password = Loadxml.getValue("dbpassword");
        } else {
            MySQL.password = "";
        }

        logger.log(Level.CONFIG, "MySQL dbpassword = " + MySQL.password);

        if (Loadxml.getValue("dbname") != null) {
            MySQL.dbname = Loadxml.getValue("dbname").trim();
        } else {
            MySQL.dbname = "job";
        }

        logger.log(Level.CONFIG, "MySQL dbname = " + MySQL.dbname);

        if (Loadxml.getValue("user") != null) {
            user_id = Loadxml.getValue("user").trim();
            username.setText(user_id);
        }

        logger.log(Level.CONFIG, "Username = " + user_id);

        if (Loadxml.getValue("password") != null) {
            user_pwd = Loadxml.getValue("password");
            if ("true".equals(Loadxml.getValue("encrypt"))) {
                BASE64Decoder base64Decoder = new BASE64Decoder();
                user_pwd = new String(base64Decoder.decodeBuffer(new String(base64Decoder.decodeBuffer(user_pwd))));
            }
            password.setText(user_pwd);
        }

        logger.log(Level.CONFIG, "Password = " + user_pwd);

        String http = Loadxml.getValue("connection");

        if (Loadxml.getValue("httpserver") != null) {
            HTTPEngine.setUrl(Loadxml.getValue("httpserver").trim());
        } else {
            HTTPEngine.setUrl("http://localhost");
        }

        logger.log(Level.CONFIG, "HTTP server = " + HTTPEngine.getUrl());

        if ("true".equals(Loadxml.getValue("encrypt"))) {
            MySQL.encrypt = "true";
        }

        if (http != null && http.equalsIgnoreCase("http")) {
            engine = new HTTPEngine();
        } else {
            engine = new Engine();
            http = "MySQL";
        }

        logger.log(Level.CONFIG, "Connection type = " + http);
        logger.log(Level.CONFIG, "Server = " + engine.getServer());

        Console.run(new Job(), 425, 475);
    }

    public void doLogin() {
        if (user_id.length() > 0) {
            status.append(user_id + " login in progress...\n");
            logged = engine.login(user_id, user_pwd);
            if (logged) {
                status.append("Successful login!\n");
                username.setEnabled(false);
                password.setEnabled(false);
                jcombo.removeAllItems();

                try {
                    //stmt = connection.createStatement();

                    projects = engine.getProjects(user_id);

                    if (projects != null && projects.size() > 0) {

                        for (int i = 0; i < projects.size(); i++) {
                            jcombo.addItem(projects.get(i));
                        }


                        usr_status = engine.getStatus(user_id);

                        if (usr_status != null) {

                            if (usr_status.getStatus() == 0) { // User Idle

                                jcombo.setEnabled(true);
                                jcombo.setFocusable(false);
                                jbstart.setEnabled(true);

                            } else {

                                user_project_id = usr_status.getProject_id();
                                user_event_id = usr_status.getEvent_id();

                                for (int i = 0; i < jcombo.getItemCount(); i++) {
                                    if (((Project) jcombo.getItemAt(i)).getKey() == user_project_id) {
                                        jcombo.setSelectedIndex(i);
                                        status.append("User has an active project called " +
                                                ((Project) jcombo.getItemAt(i)).getDescription() + "\n");
                                        usr_prj = new Project(user_project_id, ((Project) jcombo.getItemAt(i)).getDescription());
                                    }
                                }

                                jcombo.setEnabled(false);
                                jbstop.setEnabled(true);
                                jbnote.setEnabled(true);

                            }

                            jblogin.setEnabled(false);
                            jbcancel.setEnabled(false);
                            jblogout.setEnabled(true);

                        } else {
                            status.append("WARNING : UNABLE TO RETRIEVE STATUS");
                            logger.warning("UNABLE TO RETRIEVE STATUS");
                        }

                    } else {
                        status.append("WARNING : UNABLE TO RETRIEVE PROJECTS");
                        logger.warning("UNABLE TO RETRIEVE PROJECTS");
                    }


                } catch (Exception e) {
                    logger.log(Level.SEVERE, "{0}", e);
                }
            } else {
                status.append("Username and/or password incorrect! Try again...\n");
                status.append("Connection closed...\n");
            }
        } else {
            status.append("Username can't be empty!\n");
        }
    }
}