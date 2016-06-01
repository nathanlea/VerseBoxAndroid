package com.fibonaccistudios.nathanlea.versebox;

/**
 * Created by ngoalie on 5/31/2016.
 */
public class BibleChapter {
    private int number = 0;
    private int verses = 0;

    public BibleChapter(int n, int v) {
        number = n;
        verses = v;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getVerses() {
        return verses;
    }

    public void setVerses(int verses) {
        this.verses = verses;
    }
}
