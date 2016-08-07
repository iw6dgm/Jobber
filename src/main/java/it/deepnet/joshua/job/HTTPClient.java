/*
 * Created on 21-lug-2006
 *
 */
package it.deepnet.joshua.job;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mcamangi
 */
public class HTTPClient {

    private Credentials creds;
    private HttpClient client;
    private HttpMethod method;
    //private String responseBody = null;

    public String user, pwd;
    static private Logger httplogger = Logger.getLogger(org.apache.commons.httpclient.HttpClient.class.getName());

    public void connect(String url) {

        int retry = 0;
        boolean good_retry = false;

        creds = new UsernamePasswordCredentials(user, pwd);
        //create a singular HttpClient object
        client = new HttpClient();

        //establish a connection within 5 seconds
        client.getHttpConnectionManager().
                getParams().setConnectionTimeout(5000);

        // set per default
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        //      set the default credentials
        client.getState().setCredentials(AuthScope.ANY, creds);

        method = null;

        //create a method object
        method = new GetMethod(url);
        method.setFollowRedirects(true);
        do {
            try {
                int statusCode = client.executeMethod(method);

                if (statusCode != HttpStatus.SC_OK) {
                    httplogger.log(Level.SEVERE, "Method failed", method.getStatusLine());
                }

                good_retry = true;
                //responseBody = method.getResponseBodyAsString();
            } catch (HttpException he) {
                //httplogger.error("Http error connecting to '" + url + "'");
                httplogger.log(Level.WARNING, "Attempt", retry + " : " + he.getMessage());
                //System.exit(-4);
            } catch (IOException ioe) {
                //httplogger.error("Unable to connect to '" + url + "'");
                httplogger.log(Level.WARNING, "Attempt", retry + " : " + ioe.getMessage());
                //System.exit(-3);
            }
        } while (!good_retry && (retry++ < 2));
    }

    public void connectAsPost(String url, NameValuePair[] data) {

        int retry = 0;
        boolean good_retry = false;

        creds = new UsernamePasswordCredentials(user, pwd);
        //create a singular HttpClient object
        client = new HttpClient();

        //establish a connection within 5 seconds
        client.getHttpConnectionManager().
                getParams().setConnectionTimeout(5000);

        // set per default
        client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        //      set the default credentials
        client.getState().setCredentials(AuthScope.ANY, creds);

        method = null;

        //create a method object
        method = new PostMethod(url);
        method.setQueryString(data);
        method.setFollowRedirects(true);
        do {
            try {
                int statusCode = client.executeMethod(method);

                if (statusCode != HttpStatus.SC_OK) {
                    httplogger.log(Level.SEVERE, "Method failed", method.getStatusLine());
                }

                good_retry = true;
                //responseBody = method.getResponseBodyAsString();
            } catch (HttpException he) {
                //httplogger.error("Http error connecting to '" + url + "'");
                httplogger.log(Level.WARNING, "Attempt", retry + " : " + he.getMessage());
                //System.exit(-4);
            } catch (IOException ioe) {
                //httplogger.error("Unable to connect to '" + url + "'");
                httplogger.log(Level.WARNING, "Attempt", retry + " : " + ioe.getMessage());
                //System.exit(-3);
            }
        } while (!good_retry && (retry++ < 2));

    }

    public void close() {
        method.releaseConnection();
    }

    public String doQuery(String url) throws IOException {
        String responseBody = null;

        connect(url);
        responseBody = method.getResponseBodyAsString();
        close();
        return responseBody;
    }

    public String doQueryAsPost(String url, NameValuePair[] data) throws IOException {
        String responseBody = null;

        connectAsPost(url, data);
        responseBody = method.getResponseBodyAsString();
        close();
        return responseBody;
    }

    public String[] doQueryStream(String url) {

        InputStream responseBody = null;
        String[] lines = new String[32];
        int n = 0;

        connect(url);
        try {
            responseBody = method.getResponseBodyAsStream();
        } catch (IOException e) {
            httplogger.log(Level.SEVERE, "{0}", e);
        }
        if (responseBody != null) {
            BufferedReader input = new BufferedReader(new InputStreamReader(responseBody));
            try {
                lines[n++] = input.readLine();
                while (input.ready()) {
                    lines[n++] = input.readLine();
                }
            } catch (IOException e1) {
                httplogger.log(Level.SEVERE, "{0}", e1);
            }
        }
        close();
        return lines;
    }

    public String[] doQueryStreamAsPost(String url, NameValuePair[] data) {

        InputStream responseBody = null;
        String[] lines = new String[32];
        int n = 0;

        connectAsPost(url, data);
        try {
            responseBody = method.getResponseBodyAsStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            httplogger.log(Level.SEVERE, "{0}", e);
        }
        if (responseBody != null) {
            BufferedReader input = new BufferedReader(new InputStreamReader(responseBody));
            try {
                lines[n++] = input.readLine();
                while (input.ready()) {
                    lines[n++] = input.readLine();
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                httplogger.log(Level.SEVERE, "{0}", e1);
            }
        }
        close();
        return lines;
    }

    public HTTPClient(String username, String password) {
        user = username;
        pwd = password;
    }
}
