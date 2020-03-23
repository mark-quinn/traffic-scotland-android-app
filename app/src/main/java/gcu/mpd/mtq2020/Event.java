package gcu.mpd.mtq2020;

public class Event {
    public String title;
    public String description;
    public double latitude;
    public double longitude;
    public String publishedDate;

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

    public void setLocation(String geoLocation) {
        String[] splitStr = geoLocation.split("\\s+");
        this.latitude = Double.parseDouble(splitStr[0]);
        this.longitude = Double.parseDouble(splitStr[1]);
    }
}
