package gcu.mpd.mtq2020;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {
    private static final String TAG = "Event";

    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String publishedDate;
    private EventType type;
    private Date startDate;
    private Date endDate;

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
        if (type == EventType.CURRENT_INCIDENT) {
            this.description = description;
        } else {
            this.description = parseDescription(description);
        }
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

    private String parseDescription(String description) {
        String[] tokens = description.split("<br />");

        for (String token : tokens) {
            System.out.println(token);

            if (token.contains("Start Date")) {
                String date = token.split("Start Date: ")[1];
                try {
                    startDate = new SimpleDateFormat("E, dd MMM yyyy - HH:mm", Locale.UK)
                            .parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // TODO: set startDate to null if failed format?
                }
            } else if (token.contains("End Date")) {
                String date = token.split("End Date: ")[1];
                try {
                    endDate = new SimpleDateFormat("E, dd MMM yyyy - HH:mm", Locale.UK)
                            .parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // TODO: set endDate to null if failed format?
                }
            }
        }
        String desc = tokens[tokens.length-1];
        return tokens[tokens.length-1];
    }
}
