package com.fibonaccistudios.nathanlea.versebox;

import java.util.ArrayList;

/**
 * Created by ngoalie on 5/31/2016.
 */
public class BibleBook {
    public String title = "";
    public ArrayList<BibleChapter> chapter = new ArrayList<>();

    public BibleBook (String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<BibleChapter> getChapter() {
        return chapter;
    }

    public void setChapter(ArrayList<BibleChapter> chapter) {
        this.chapter = chapter;
    }
}
