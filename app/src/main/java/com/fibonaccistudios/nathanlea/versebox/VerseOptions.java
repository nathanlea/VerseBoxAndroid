package com.fibonaccistudios.nathanlea.versebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VerseOptions extends AppCompatActivity {

    int bookIndex=1, chapterNumber=1, startVerse=1, endVerse=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_options);

        bookIndex = (int)getIntent().getExtras().get("bookIndex");
        chapterNumber = (int)getIntent().getExtras().get("chapterNumber");
        startVerse = (int)getIntent().getExtras().get("startVerse");
        endVerse = (int)getIntent().getExtras().get("endVerse");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, VersePicker.class);
        i.putExtra("bookIndex", bookIndex);
        i.putExtra("chapterNumber", chapterNumber);
        i.putExtra("startVerse", startVerse);
        i.putExtra("endVerse", endVerse);
        setResult(RESULT_OK, i);
    }
}
