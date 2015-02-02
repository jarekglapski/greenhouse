package greenhouse.status.remote;

import greenhouse.status.Measurement;
import greenhouse.status.Reader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 */
public class BasicGoogleSpreadsheetPoster extends TimerTask {

    private String url = "";
    private static final Logger LOG = Logger.getLogger(BasicGoogleSpreadsheetPoster.class.getName());

    private final Reader sensorsReader;

    public BasicGoogleSpreadsheetPoster(Reader sensorsReader) {
        this.sensorsReader = sensorsReader;
    }

    private int sendPostRequest(String data) throws Exception {
        URL endpoint = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();

        //TODO: params as json? set req type etc!
        LOG.log(Level.FINE, "Sending POST request to URL: {0}", url);
        LOG.log(Level.FINER, "Post parameters: {0}", data);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(data);
            wr.flush();
        }
        int responseCode = connection.getResponseCode();

        StringBuffer response = readResponse(connection);
        LOG.log(Level.FINE, "Response Code: {0}", responseCode);
        LOG.finer(response.toString());
        connection.disconnect();
        return responseCode;
    }

    private int sendGetRequest(String data) throws ProtocolException, IOException {
        URL endpoint = new URL(url + "?" + data);
        HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();

        LOG.log(Level.FINE, "Sending GET request to URL: {0}", url);
        LOG.log(Level.FINER, "Post parameters: {0}", data);
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        StringBuffer response = readResponse(connection);
        LOG.log(Level.FINE, "Response Code: {0}", responseCode);
        LOG.finer(response.toString());
        connection.disconnect();
        return responseCode;
    }

    private StringBuffer readResponse(HttpsURLConnection connection) throws IOException {
        StringBuffer response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response;
    }

    @Override
    public void run() {
        sensorsReader.getLastMeasurement().stream().forEach((measurement) -> {
            try {
                sendGetRequest(getData(measurement));
            } catch (Exception ex) {
                Logger.getLogger(BasicGoogleSpreadsheetPoster.class.getName()).log(Level.SEVERE, "Cannot post measurement.", ex);
            }
        });
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String getData(Measurement measurement) {
        return "datetime=test&temp1=100";
    }
}
