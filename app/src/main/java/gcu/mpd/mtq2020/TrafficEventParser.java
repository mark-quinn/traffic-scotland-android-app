package gcu.mpd.mtq2020;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

public class TrafficEventParser {
    private InputStream feed;

    public TrafficEventParser(InputStream feed) {
        this.feed = feed;
    }

    public void parseFeed() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(feed, null); // inputEncoding automatically gets set
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT)
                    System.out.println("Start document");
                else if(eventType == XmlPullParser.START_TAG) {
                    String temp = xpp.getName();
                    System.out.println("Start tag " + temp);
                }
                else if(eventType == XmlPullParser.END_TAG) {
                    String temp = xpp.getName();
                    System.out.println("End tag " + temp);
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            Log.e("Parser", "Cannot parse event", e);
        } catch (IOException e) {
            Log.e("IO", "IO error during parsing", e);
        }
        System.out.println("End document");
    }
}
