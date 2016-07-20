package com.fibonaccistudios.nathanlea.versebox;

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
    private boolean loved = false;
    private int list = 0; //0-Daily 1-Odd/Even 2-Week 3-Month
    private int levelInList = 0; //Daily 0 // 1-Odd or 0-Even //Week Level Sun-0 Sat-6 //Month 1-31

    private transient String VerseReference = null;

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

    public void rebuildVerseCard (String startDate, String endDate,
                                  String verseRef, String verseStr,
                                  String topic, String section) {
        this.startDate = startDate;
        this.endDate  = endDate;
        this.topic = topic;
        this.section = section;
        this.verseStr = verseStr;
        this.VerseReference = verseRef;
    }

    public void buildVerseCardStrings( )
    {
        //TODO Make this not have to do this much work every time
        final Bible bible = MainActivity.bible;
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

    public int  getList() { return list; }
    public int  getLevelInList() { return levelInList; }
    public int getBookIndex() {return bookIndex;}
    public int getStartVerse() {return start_verse;}
    public int getEndVerse() {return end_verse;}
    public int getChapter() { return chapter;}
    public boolean isLoved() {
        return loved;
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
        if(VerseReference==null)
            buildVerseCardStrings();
        return VerseReference;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }
    public void promote(int level) { this.list = level;}
    public void setLevelInList(int level) { this.levelInList = level;}
}
