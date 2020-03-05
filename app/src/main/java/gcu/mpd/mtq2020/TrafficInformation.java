package gcu.mpd.mtq2020;

import android.app.Activity;

public class TrafficInformation {
    private final Activity activity;
    private String baseString = "https://trafficscotland.org/rss/feeds";
    private String currentIncidentsURL  = baseString + "/currentincidents.aspx";
    private String currentRoadworksURL = baseString + "/roadworks.aspx";
    private String plannedRoadworksURL = baseString + "/plannedroadworks.aspx";

    public TrafficInformation(Activity activity) {
        this.activity = activity;
    }

    public void getCurrentIncidents() {
        new FetchRSSFeed(currentIncidentsURL, activity).execute((Void) null);
    }

    public void getCurrentRoadworks() {
        new FetchRSSFeed(currentRoadworksURL, activity).execute((Void) null);
    }

    public void getPlannedRoadworks() {
        new FetchRSSFeed(plannedRoadworksURL, activity).execute((Void) null);
    }
}
