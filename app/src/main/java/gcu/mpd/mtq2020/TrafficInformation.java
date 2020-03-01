package gcu.mpd.mtq2020;

public class TrafficInformation {
    private String baseString = "https://trafficscotland.org/rss/feeds";
    private String currentIncidentsURL  = baseString + "/currentincidents.aspx";
    private String currentRoadworksURL = baseString + "roadworks.aspx";
    private String plannedRoadworksURL = baseString + "/plannedroadworks.aspx";

    public void getCurrentIncidents() {
        new FetchRSSFeed(currentIncidentsURL).execute((Void) null);
    }

    public void getCurrentRoadworks() {
        new FetchRSSFeed(currentRoadworksURL).execute((Void) null);
    }

    public void getPlannedRoadworks() {
        new FetchRSSFeed(plannedRoadworksURL).execute((Void) null);
    }
}
