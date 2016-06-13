package com.fibonaccistudios.nathanlea.versebox;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ngoalie on 6/2/2016.
 */

public class BibleXMLParser {
    private static final String ns = null;

    // We don't use namespaces

    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, ns, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("verses")) {
                parser.require(XmlPullParser.START_TAG, ns, "verses");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name_1 = parser.getName();
                    // Starts by looking for the entry tag
                    if (name_1.equals("verse")) {
                        entries.add(readEntry(parser));
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // This class represents a single entry (post) in the XML feed.
    // It includes the data members "title," "link," and "summary."
    public static class Entry implements Serializable {
        public final String verse;
        public final String reference;
        public final String text;

        private Entry(String verse, String reference, String text) {
            this.verse = verse;
            this.reference = reference;
            this.text = text;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "verse");
        String verse = null;
        String reference = null;
        String passage = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("verse")) {
                verse = readVerse(parser);
            } else if (name.equals("reference")) {
                reference = readReference(parser);
            } else if (name.equals("text")) {
                passage = readPassage(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(verse, reference, passage);
    }

    // Processes title tags in the feed.
    private String readVerse(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "verse");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "verse");
        return title;
    }

    private String readReference(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "reference");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "reference");
        return title;
    }

    private String readPassage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "text");
        String title = readText(parser);
        Matcher m = Pattern.compile("</sup>(.*)</p>").matcher(title);
        m.find();
        String s = m.group(1).trim();
        parser.require(XmlPullParser.END_TAG, ns, "text");
        return s;
    }

/* Not needed, got from example
    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }
*/
    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
