package jenkins.plugins.slakk;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

import jenkins.model.Jenkins;
import hudson.ProxyConfiguration;

public class StandardSlakkService implements SlakkService {

    private static final Logger logger = Logger.getLogger(StandardSlakkService.class.getName());

    private String host = "slakk.com";
    private String teamDomain;
    private String token;
    private String[] roomIds;

    public StandardSlakkService(String teamDomain, String token, String roomId) {
        super();
        this.teamDomain = teamDomain;
        this.token = token;
        this.roomIds = roomId.split(",");
    }

    public void publish(String message) {
        publish(message, "yellow");
    }

    public void publish(String message, String color) {
        for (String roomId : roomIds) {
            String url = "https://" + teamDomain + "." + host + "/services/hooks/jenkins-ci?token=" + token;
            logger.info("Posting: to " + roomId + " on " + teamDomain + " using " + url +": " + message + " " + color);
            HttpClient client = getHttpClient();
            PostMethod post = new PostMethod(url);
            JSONObject json = new JSONObject();

            try {
                json.put("channel", roomId);
                json.put("text", message);
                json.put("color", color);

                post.addParameter("payload", json.toString());
                post.getParams().setContentCharset("UTF-8");
                int responseCode = client.executeMethod(post);
                String response = post.getResponseBodyAsString();
                if(responseCode != HttpStatus.SC_OK) {
                    logger.log(Level.WARNING, "Slakk post may have failed. Response: " + response);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error posting to Slakk", e);
            } finally {
                post.releaseConnection();
            }
        }
    }
    
    private HttpClient getHttpClient() {
        HttpClient client = new HttpClient();
        if (Jenkins.getInstance() != null) {
            ProxyConfiguration proxy = Jenkins.getInstance().proxy;
            if (proxy != null) {
                client.getHostConfiguration().setProxy(proxy.name, proxy.port);
            }
        }
        return client;
    }

    void setHost(String host) {
        this.host = host;
    }
}
