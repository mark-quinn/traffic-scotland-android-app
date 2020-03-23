package gcu.mpd.mtq2020;

public interface TrafficURL {
    String BASE_URL = "https://trafficscotland.org/rss/feeds";
    String currentIncidents = BASE_URL + "/currentincidents.aspx";
    String ongoingRoadworks = BASE_URL + "/roadworks.aspx";
    String plannedRoadworks = BASE_URL + "/plannedroadworks.aspx";
}
