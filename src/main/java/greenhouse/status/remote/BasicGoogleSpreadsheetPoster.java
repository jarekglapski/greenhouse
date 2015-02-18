package greenhouse.status.remote;

import greenhouse.status.Measurement;
import greenhouse.status.Reader;
import org.json.JSONObject;
import org.json.JSONStringer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /**
     *
     */
    private int sendPostRequest(Set<Measurement> measurements) throws Exception {
        String jsonData = getJSONData(measurements);
        URL endpoint = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();

        //TODO: params as json? set req type etc!
        LOG.log(Level.FINE, "Sending POST request to URL: {0}", url);
        LOG.log(Level.FINER, "Post parameters: {0}", jsonData);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(jsonData);
            wr.flush();
        }
        int responseCode = connection.getResponseCode();

        StringBuffer response = readResponse(connection);
        LOG.log(Level.FINE, "Response Code: {0}", responseCode);
        LOG.finer(response.toString());
        connection.disconnect();
        return responseCode;
    }

    private int sendGetRequest(Set<Measurement> measurements) throws IOException {
        int responseCode = 0;
        for (Measurement measurement : measurements) {
            responseCode = sendGetRequest(measurement);
            if (responseCode != 200) {
                break;
            }
        }
        return responseCode;
    }

    private int sendGetRequest(Measurement measurement) throws IOException {
        String paramData = getData(measurement);
        URL endpoint = new URL(url + "?" + paramData);
        HttpsURLConnection connection = (HttpsURLConnection) endpoint.openConnection();

        LOG.log(Level.FINE, "Sending GET request to URL: {0}", url);
        LOG.log(Level.FINER, "Post parameters: {0}", paramData);
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
        try {
            sendPostRequest(sensorsReader.getLastMeasurement());
        } catch (Exception ex) {
            Logger.getLogger(BasicGoogleSpreadsheetPoster.class.getName()).log(Level.SEVERE, "Cannot post measurement.", ex);
        }
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

    private String getJSONData(Measurement measurement) {
        JSONObject object = new JSONObject();
        object.put("datetime", measurement.getDate().getTime());
        object.put(measurement.getSensorId(), measurement.getValue());
        return object.toString();
    }

    /**
     *
     * @param measurements
     * @return JSON array like:
     * [{"datetime":"date0","temp1":"50"},{"datetime":"date1", "temp1":"60"}]
     */
    private String getJSONData(Set<Measurement> measurements) {
        JSONStringer stringer = new JSONStringer();
        stringer.array();
        for (Measurement measurement : measurements) {
            stringer.object().key("datetime").value(measurement.getDate().getTime()).key(measurement.getSensorId()).value(measurement.getValue()).endObject();
        }
        stringer.endArray();
        return stringer.toString();
    }
}
