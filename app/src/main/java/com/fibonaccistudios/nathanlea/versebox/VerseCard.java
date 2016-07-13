package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by nathan on 6/14/16.
 */
public class VerseCard implements Serializable{
    private int bookIndex = 0;
    private int start_verse = 0;
    private int end_verse = 0;
    private int chapter = 0;
    private String startDate = null;
    private String endDate = null;
    private String topic = null;
    private String section = null;
    private String verseStr = null;

    private transient String VerseReference = null;

    private transient Context c = null;

    public VerseCard(int bookIndex, int chapter, int start_verse, int end_verse,
                     String startDate, String endDate, String verseStr,
                     String topic, String section)
    {
        this.bookIndex = bookIndex;
        this.chapter = chapter;
        this.start_verse = start_verse;
        this.end_verse = end_verse;
        this.startDate = startDate;
        this.endDate  = endDate;
        this.topic = topic;
        this.section = section;
        this.verseStr = verseStr;
    }

    public void setAppContext( Context context ) {
        this.c = context;
    }

    public void buildVerseCardStrings( )
    {
        //TODO Make this not have to do this much work every time
        final Bible bible = new bibleVerseJSON(c).getBible();
        int tempStart = start_verse+1;
        int tempEnd = end_verse+1;
        String verse;
        if(start_verse == end_verse) {
            verse = bible.books.get(bookIndex-1).getTitle() + " " + chapter + " : " + tempStart;
        } else {
            verse = bible.books.get(bookIndex-1).getTitle() + " " + chapter + " : " + tempStart + " - " + tempEnd;
        }

        VerseReference = verse;
    }

    public void buildVerseCardStrings( Context c )
    {
        //TODO Make this not have to do this much work every time
        final Bible bible = new bibleVerseJSON(c).getBible();
        int tempStart = start_verse+1;
        int tempEnd = end_verse+1;
        String verse;
        if(start_verse == end_verse) {
            verse = bible.books.get(bookIndex-1).getTitle() + " " + chapter + " : " + tempStart;
        } else {
            verse = bible.books.get(bookIndex-1).getTitle() + " " + chapter + " : " + tempStart + " - " + tempEnd;
        }

        VerseReference = verse;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTopic() {
        return topic;
    }

    public String getSection() {
        return section;
    }

    public String getVerseStr() {
        return verseStr;
    }

    public String getVerseReference() {
        return VerseReference;
    }
}
