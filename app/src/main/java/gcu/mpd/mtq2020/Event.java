package gcu.mpd.mtq2020;

import java.util.Locale;

public class Event {
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String publishedDate;
    private EventType type;

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

    public void setLocation(String geoLocation) {
        String[] splitStr = geoLocation.split("\\s+");
        this.latitude = Double.parseDouble(splitStr[0]);
        this.longitude = Double.parseDouble(splitStr[1]);
    }

    public void setEventType(EventType type) {
        this.type = type;
    }
}
