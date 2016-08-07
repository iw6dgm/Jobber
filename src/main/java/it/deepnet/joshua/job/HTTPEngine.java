/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.deepnet.joshua.job;

import org.apache.commons.httpclient.NameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

/**
 * @author mcamangi
 */
public class HTTPEngine extends Engine {

    private static String url;

    private static String httpUser = "admin";
    private static String httpPwd = "jobber";

    private HTTPClient hc;

    public HTTPEngine() {

        hc = new HTTPClient(httpUser, httpPwd);
        setServer(getUrl());

    }

    public static void setUrl(String u) {
        url = u;
    }

    public static String getUrl() {
        return url;
    }

    @Override
    public boolean login(String user, String password) {
        boolean logged = false;

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        String actionUrl = getUrl() + "?action=login" +
                "&login=" + user +
                "&password=" + password;

        Job.logger.log(Level.FINE, actionUrl);

        try {
            if (hc.doQuery(actionUrl).equalsIgnoreCase("ok")) {
                logged = true;
            }
        } catch (IOException ex) {
            Job.logger.log(Level.SEVERE, "{HTTPEngine}", ex);
        }

        return logged;
    }

    @Override
    public List<Project> getProjects(String user) {

        List<Project> prj = null;

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        String actionUrl = getUrl() + "?action=getProjects" +
                "&login=" + Job.user_id +
                "&password=" + Job.user_pwd +
                "&user=" + user;

        Job.logger.log(Level.FINE, actionUrl);

        String projects[] = hc.doQueryStream(actionUrl);

        if (projects != null &&
                !projects[0].equalsIgnoreCase("ko")) {

            prj = new ArrayList<>();

            for (int i = 0; i < projects.length; i++) {

                if (projects[i] != null && projects[i].length() > 0 && projects[i].contains("|")) {
                    StringTokenizer project = new StringTokenizer(projects[i], "|", false);
                    if (project.countTokens() == 2) {

                        String project_id = (String) project.nextElement();
                        String project_descr = (String) project.nextElement();

                        prj.add(new Project(Integer.parseInt(project_id), project_descr));

                    }

                }

            }
        }

        return prj;

    }

    @Override
    public Status getStatus(String user) {

        Status status = null;

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        String actionUrl = getUrl() + "?action=getStatus" +
                "&login=" + Job.user_id +
                "&password=" + Job.user_pwd +
                "&user=" + user;

        Job.logger.log(Level.FINE, actionUrl);

        String[] s = hc.doQueryStream(actionUrl);

        if (s != null &&
                s.length >= 3) {
            status = new Status();
            status.setStatus(Integer.parseInt(s[0]));
            status.setProject_id(Integer.parseInt(s[1]));
            status.setEvent_id(Integer.parseInt(s[2]));
        }

        return status;

    }

    @Override
    public String getNote(int id) {

        String note = "<empty>";

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        String actionUrl = getUrl() + "?action=getNotes" +
                "&login=" + Job.user_id +
                "&password=" + Job.user_pwd +
                "&id=" + id;

        Job.logger.log(Level.FINE, actionUrl);

        try {
            note = hc.doQuery(actionUrl);
        } catch (IOException ex) {
            Job.logger.log(Level.SEVERE, "{HTTPEngine}", ex);
            note = "<error retreiving notes>";
        }

        return note;
    }

    @Override
    public int startProject(String user, int project_id) {

        int event_id = -1;

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        String actionUrl = getUrl() + "?action=startProject" +
                "&login=" + Job.user_id +
                "&password=" + Job.user_pwd +
                "&user=" + user +
                "&project_id=" + project_id;

        Job.logger.log(Level.FINE, actionUrl);

        try {
            String event = hc.doQuery(actionUrl);
            event_id = Integer.parseInt(event);
        } catch (IOException ex) {
            Job.logger.log(Level.SEVERE, "{HTTPEngine}", ex);
        }

        return event_id;

    }

    @Override
    public void stopProject(String user) {

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        String actionUrl = getUrl() + "?action=stopProject" +
                "&login=" + Job.user_id +
                "&password=" + Job.user_pwd +
                "&user=" + user;

        Job.logger.log(Level.FINE, actionUrl);

        try {
            String ok = hc.doQuery(actionUrl);

            if (!ok.equalsIgnoreCase("ok")) {
                Job.logger.log(Level.WARNING, "stopProject failed");
            }
        } catch (IOException ex) {
            Job.logger.log(Level.SEVERE, "{HTTPEngine}", ex);
        }

    }

    @Override
    public void updateNote(int id, String note) {

        //HTTPClient hc = new HTTPClient(httpUser, httpPwd);

        NameValuePair[] data = {
                new NameValuePair("action", "updateNote"),
                new NameValuePair("login", Job.user_id),
                new NameValuePair("password", Job.user_pwd),
                new NameValuePair("id", id + ""),
                new NameValuePair("notes", note)
        };

        try {
            String ok = hc.doQueryAsPost(getUrl(), data);

            if (!ok.equalsIgnoreCase("ok")) {
                Job.logger.log(Level.WARNING, "updateNote failed");
            }
        } catch (IOException ex) {
            Job.logger.log(Level.SEVERE, "{HTTPEngine}", ex);
        }

    }

}
