package com.fibonaccistudios.nathanlea.versebox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.grantland.widget.AutofitHelper;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VerseMemory extends AppCompatActivity {
    private View mContentView;

    private int screenIndex = 0;
    private int verseIndex = 0;
    private int verseTotal = 0;
    private boolean infoToReveal[] = {false, true, true, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<VerseCard> totalList = MainActivity.verseList;

        //Somehow look at the dates of the verses and decide which are today verses
        final List<VerseCard> todaysVerses = totalList; //TODO TEMP

        // TODO Randomize them?
        Collections.shuffle(todaysVerses);
        verseTotal = todaysVerses.size();

        //TODO FOR NOW
        if(verseTotal==0) { finish(); }

        setContentView(R.layout.activity_verse_memory);

        setTitle("Today's Verses");

        mContentView = findViewById(R.id.main_verse_view);

        if(screenIndex == 0)
           blankCard(todaysVerses);

        mContentView.setOnTouchListener(new OnSwipeTouchListener(VerseMemory.this) {
            public void onSwipeTop() {
                if(screenIndex!=2) {
                    ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_verse_full, parent, false);
                    parent.addView(mContentView, index);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 2;
                } else if ( screenIndex == 2) {
                    //next card
                    screenIndex = 0;
                    ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_subject, parent, false);
                    parent.addView(mContentView, index);
                    mContentView.setOnTouchListener(this);
                    verseIndex++;
                    if(verseIndex==verseTotal) {
                        //done with the list for today!
                        //popup and head back to home screen

                        finish();
                    }
                    blankCard(todaysVerses);
                }
            }
            public void onSwipeRight() {
                if(screenIndex!=0) {
                    ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_subject, parent, false);
                    parent.addView(mContentView, index);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 0;
                    blankCard(todaysVerses);
                }
            }
            public void onSwipeLeft() {
                if(screenIndex!=1) {
                    ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_subject, parent, false);
                    parent.addView(mContentView, index);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 1;
                    buildCard(todaysVerses);
                }
            }
        });
    }

    private void buildCard(List<VerseCard> v) {
        View front = mContentView.findViewById(R.id.card_view_front);
        TextView VR = (TextView) front.findViewById(R.id.verseRefFrag);
        TextView VS = (TextView) front.findViewById(R.id.sectionFrag);
        TextView VT = (TextView) front.findViewById(R.id.topicFrag);
        TextView VSD = (TextView) front.findViewById(R.id.startDateFrag);
        TextView VED = (TextView) front.findViewById(R.id.endDateFrag);

        v.get(verseIndex).buildVerseCardStrings();

        VR.setText( v.get(verseIndex).getVerseReference());
        VS.setText( v.get(verseIndex).getSection());
        VT.setText( v.get(verseIndex).getTopic());
        VSD.setText(v.get(verseIndex).getStartDate());
        VED.setText(v.get(verseIndex).getEndDate());

        AutofitHelper verseAUTO = AutofitHelper.create(VR);
        verseAUTO.setMaxLines(1);
    }

    private void blankCard(List<VerseCard> v) {
        View front = mContentView.findViewById(R.id.card_view_front);
        TextView VR = (TextView) front.findViewById(R.id.verseRefFrag);
        TextView VS = (TextView) front.findViewById(R.id.sectionFrag);
        TextView VT = (TextView) front.findViewById(R.id.topicFrag);
        TextView VSD = (TextView) front.findViewById(R.id.startDateFrag);
        TextView VED = (TextView) front.findViewById(R.id.endDateFrag);

        v.get(verseIndex).buildVerseCardStrings();

        VR.setText( v.get(verseIndex).getVerseReference());
        VS.setText( v.get(verseIndex).getSection());
        VT.setText( v.get(verseIndex).getTopic());
        VSD.setText(v.get(verseIndex).getStartDate());
        VED.setText(v.get(verseIndex).getEndDate());

        AutofitHelper verseAUTO = AutofitHelper.create(VR);
        verseAUTO.setMaxLines(1);

        TextView card[] = {VR, VS, VT, VSD, VED};
        RevealTouchListener touchListener = new RevealTouchListener();

        for( int i = 0; i < card.length; i++ ) {
            if(!infoToReveal[i]) {
                card[i].setTextColor(Color.LTGRAY);
                card[i].setBackgroundColor(Color.LTGRAY);
                card[i].setOnTouchListener(touchListener);
            }
        }
    }

    class RevealTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.setBackgroundColor(Color.WHITE);
            ((TextView)v).setTextColor(Color.BLACK);
            return false;
        }
    }
}
