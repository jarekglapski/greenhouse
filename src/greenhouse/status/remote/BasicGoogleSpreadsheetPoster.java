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

    private String url = "TODO: insert URL here";
    private static final Logger LOG = Logger.getLogger(BasicGoogleSpreadsheetPoster.class.getName());

    private final Reader sensorsReader;

    public BasicGoogleSpreadsheetPoster(Reader sensorsReader) {
        this.sensorsReader = sensorsReader;
    }

    private int post(Measurement measurement) throws Exception {

        URL endpoint = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();

        sendRequest(connection, measurement);

        int responseCode = connection.getResponseCode();

        StringBuffer response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        LOG.log(Level.FINE, "Response Code: {0}", responseCode);
        LOG.finer(response.toString());

        return responseCode;
    }

    private void sendRequest(HttpsURLConnection connection, Measurement measurement) throws ProtocolException, IOException {
        //TODO: params!
        String urlParameters = "some=stuff&here=please";
        LOG.log(Level.FINE, "Sending POST request to URL: {0}", url);
        LOG.log(Level.FINER, "Post parameters: {0}", urlParameters);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }
    }

    @Override
    public void run() {
        sensorsReader.getLastMeasurement().stream().forEach((measurement) -> {
            try {
                post(measurement);
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
}
