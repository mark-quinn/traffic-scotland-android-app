package gcu.mpd.mtq2020;

import android.util.Log;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.Duration;
import org.joda.time.Interval;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Event implements Serializable {
    private static final String TAG = "Event";

    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String publishedDate;
    private EventType type;
    private Date startDate = null;
    private Date endDate = null;
    private EventLength eventLength = null;
    private String delayInformation = null;
    private String locationInfo = null;
    private String laneClosures = null;
    private String workDetails = null;
    private String trafficManagement = null;
    private String diversionInfo = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getLocation() {
        return String.format(Locale.ENGLISH, "%f,%f", latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getSimpleStartDate() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyy");
        return df.format(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setLocation(String geoLocation) {
        String[] splitStr = geoLocation.split("\\s+");
        this.latitude = Double.parseDouble(splitStr[0]);
        this.longitude = Double.parseDouble(splitStr[1]);
    }

    public void setEventType(EventType type) {
        this.type = type;
    }

    public EventLength getEventLength() {
        return eventLength;
    }

    public void setEventLength() {
        Interval interval = new Interval(startDate.getTime(), endDate.getTime());
        Duration period = interval.toDuration();
        long days = period.getStandardDays();

        if (days <= 7) {
            eventLength = EventLength.SHORT;
        } else if (days <= 31) {
            eventLength = EventLength.INTERMEDIATE;
        } else {
            eventLength = EventLength.LONG;
        }
    }

    public void parseAdditionalFeatures() {
        String[] tokens = this.description.split("<br />");

        for (String token : tokens) {
            System.out.println(token);

            if (token.contains("Start Date")) {
                String date = token.split("Start Date: ")[1];
                try {
                    this.startDate = new SimpleDateFormat("E, dd MMM yyyy - HH:mm", Locale.UK)
                            .parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // TODO: set startDate to null if failed format?
                }
            } else if (token.contains("End Date")) {
                String date = token.split("End Date: ")[1];
                try {
                    this.endDate = new SimpleDateFormat("E, dd MMM yyyy - HH:mm", Locale.UK)
                            .parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // TODO: set endDate to null if failed format?
                }
            }
        }

        if (this.startDate != null && this.endDate != null) {
            setEventLength();
        }

        // TODO: format description further
        String additionalInfo = tokens[tokens.length - 1];
        extractAdditionalFields(additionalInfo);
    }

    private void extractAdditionalFields(String info) {
        String[] tokens = info.split("\n");

        switch (this.type) {
            case ONGOING_ROADWORK:
                parseOnGoingRoadwork(tokens);
            case PLANNED_ROADWORK:
                parsePlannedRoadwork(tokens);
        }
    }

    private void parseOnGoingRoadwork(String[] details) {
        for (int i = 0; i < details.length; i++) {
            if (details[i].isEmpty()) {
                break;
            }

            String currentField = details[i];
            this.delayInformation = currentField.split(":")[1];
        }
    }

    private void parsePlannedRoadwork(String[] details) {
        for (int i = 0; i < details.length; i++) {
            if (details[i].isEmpty()) {
                continue;
            }
            String currentField = details[i];

            switch (currentField) {
                case "Delay Information:":
                    this.delayInformation = details[i + 1];
                    break;
                case "Location:":
                    this.locationInfo = details[i + 1];
                    break;
                case "Lane Closures:":
                    this.laneClosures = details[i + 1];
                    break;
                case "Works:":
                    this.workDetails = details[i + 1];
                    break;
                case "Traffic Management:":
                    this.trafficManagement = details[i + 1];
                    break;
                case "Diversion Information:":
                    this.diversionInfo = details[i + 1];
                    break;
            }
        }
    }
}

