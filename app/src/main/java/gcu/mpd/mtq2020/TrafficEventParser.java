package gcu.mpd.mtq2020;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TrafficEventParser {
    private InputStream feed;
    private String title = null;
    private String description = null;
    private String geoLocation = null;
    private String publishedDate = null;
    private boolean isItem = false;
    List<Event> events = new ArrayList<>();

    public TrafficEventParser(InputStream feed) {
        this.feed = feed;
    }

    public List<Event> parseFeed() throws IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(feed, null); // inputEncoding automatically gets set

            xpp.nextTag(); // skip first tag
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xpp.getEventType();

                String name = xpp.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if(eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        resetItemFields();
                        continue;
                    }
                }

                String result = "";
                if(xpp.next() == XmlPullParser.TEXT) {
                    result = xpp.getText();
                    xpp.nextTag();
                }
                xpp.next();

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                } else if (name.equalsIgnoreCase("point")) {
                    geoLocation = result;
                } else if (name.equalsIgnoreCase("pubdate")) {
                    publishedDate = result;
                }

                if(validItem()) {
                    Event event = new Event(title, description, geoLocation, publishedDate);
                    events.add(event);
                }
            }
        }
        catch (XmlPullParserException e) {
            Log.e("Parser", "Cannot parse event", e);
        }
        finally {
            feed.close();
            Log.d("STREAM", "Input stream closed");
        }
        return events;
    }

    private boolean validItem() {
        if(title != null && description != null && geoLocation != null && publishedDate != null
                 && isItem) {
            return true;
        }
        return false;
    }

    private void resetItemFields() {
        title = null;
        description = null;
        geoLocation = null;
        publishedDate = null;
    }
}
