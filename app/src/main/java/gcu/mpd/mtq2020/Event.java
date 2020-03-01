package gcu.mpd.mtq2020;

public class Event {
    public String title;
    public String description;
    public double latitude;
    public double longitude;
    public String publishedDate;

    public Event(String title, String description,
                 String geoLocation, String publishedDate) {
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate;
        setLocation(geoLocation);
    }

    private void setLocation(String geoLocation) {
        String[] splitStr = geoLocation.split("\\s+");
        this.latitude = Double.parseDouble(splitStr[0]);
        this.longitude = Double.parseDouble(splitStr[1]);
    }
}
