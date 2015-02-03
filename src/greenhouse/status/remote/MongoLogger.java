package greenhouse.status.remote;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import greenhouse.status.Measurement;
import greenhouse.status.Reader;
import java.net.UnknownHostException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class MongoLogger extends TimerTask {

    private static final String DB_URI = "";
    private static final String DB_NAME = "greenhouse";
    private static final String COLLECTION_NAME = "measurements";

    private final Reader sensorsReader;

    public MongoLogger(Reader sensorsReader) {
        this.sensorsReader = sensorsReader;
    }

    @Override
    public void run() {
        try {
            MongoClient mongoClient = new MongoClient(new MongoClientURI(DB_URI));
            DB db = mongoClient.getDB(DB_NAME);
            DBCollection measurements = db.getCollection(COLLECTION_NAME);
            sensorsReader.getLastMeasurement().stream().forEach((measurement) -> {
                measurements.insert(creatMeasurementDocument(measurement));
            });
        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BasicDBObject creatMeasurementDocument(Measurement measurement) {
        BasicDBObject document = new BasicDBObject("datetime", measurement.getDate().getTime())
                .append("sensorId", measurement.getSensorId())
                .append("type", measurement.getPhysicalQuantity())
                .append("value", measurement.getValue())
                .append("unit", measurement.getUnit());
        return document;
    }
}
