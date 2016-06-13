package com.fibonaccistudios.nathanlea.versebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import me.grantland.widget.AutofitHelper;

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

        TextView verseREF = (TextView) findViewById(R.id.verseRef_TV);
        AutofitHelper verseAUTO = AutofitHelper.create(verseREF);
        verseAUTO.setMaxLines(1);


        EditText preview = (EditText) findViewById(R.id.verse_preview_tv);

        //TODO Make this not have to do this much work every time
        final Bible bible = new bibleVerseJSON(getApplicationContext()).getBible();
        int tempStart = startVerse+1;
        int tempEnd = endVerse+1;
        String verse;
        if(startVerse == endVerse) {
            verse = bible.books.get(bookIndex-1).getTitle() + " " + chapterNumber + " : " + tempStart;
        } else {
            verse = bible.books.get(bookIndex-1).getTitle() + " " + chapterNumber + " : " + tempStart + " - " + tempEnd;
        }

        if(verseREF!=null)
            verseREF.setText(verse);

        List<BibleXMLParser.Entry> entry = MainActivity.GloablBibleMap.get(MainActivity.bookID.get(bookIndex - 1).ID + "." + chapterNumber);
        if (entry != null && preview != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = startVerse; i <= endVerse; i++) {
                sb.append(entry.get(i).text);
                sb.append(" ");
            }
            preview.setText(sb.toString());
        }


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
