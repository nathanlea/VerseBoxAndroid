package com.fibonaccistudios.nathanlea.versebox;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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
    private List<VerseCard> totalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        /*
        TextView VR = (TextView) front.findViewById(R.id.verseRefFrag);
        TextView VS = (TextView) front.findViewById(R.id.sectionFrag);
        TextView VT = (TextView) front.findViewById(R.id.topicFrag);
        TextView VSD = (TextView) front.findViewById(R.id.startDateFrag);
        TextView VED = (TextView) front.findViewById(R.id.endDateFrag);
         */

        infoToReveal[0] = sharedPref.getBoolean("show_verse_ref", false);
        infoToReveal[1] = sharedPref.getBoolean("show_section", false);
        infoToReveal[2] = sharedPref.getBoolean("show_topic", false);
        infoToReveal[3] = sharedPref.getBoolean("show_dates", false);
        infoToReveal[4] = infoToReveal[3];


        setContentView(R.layout.activity_verse_memory);

        totalList = MainActivity.verseList;

        final List<VerseCard> todaysVerses = new ArrayList<>();

        final Calendar c = Calendar.getInstance();

        for(int i = 0; i < totalList.size(); i++) {
            int l = totalList.get(i).getList();
            if (l == 0) {
                todaysVerses.add(totalList.get(i));
            } else if (l == 1) {
                if (c.get(Calendar.DATE) % 2 == totalList.get(i).getLevelInList()) {
                    todaysVerses.add(totalList.get(i));
                }
            } else if (l == 2) {
                if (c.get(Calendar.DAY_OF_WEEK) == totalList.get(i).getLevelInList()) {
                    todaysVerses.add(totalList.get(i));
                }
            } else if (l == 3) {
                if (c.get(Calendar.DATE) == totalList.get(i).getLevelInList()) {
                    todaysVerses.add(totalList.get(i));
                }
            }
        }

        //TODO Setting to randomize them
        Collections.shuffle(todaysVerses);
        verseTotal = todaysVerses.size();

        setTitle("Today's Verses");

        mContentView = findViewById(R.id.main_verse_view);

        if(screenIndex == 0)
           blankCard(todaysVerses);

        mContentView.setOnTouchListener(new OnSwipeTouchListener(VerseMemory.this) {
            public void onSwipeTop() {
                //next card

                final ViewGroup parent = (ViewGroup) mContentView.getParent();
                int index = parent.indexOfChild(mContentView);
                if(screenIndex == 0)
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_scroll_up_top);
                else if (screenIndex == 1)
                    PlayAnim(R.id.card_view_back, getBaseContext(), R.animator.card_scroll_up_top);
                //parent.removeView(mContentView);
                final View temp = mContentView;
                mContentView = getLayoutInflater().inflate(R.layout.fragment_subject, parent, false);
                parent.addView(mContentView, index);
                PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_scroll_up_bottom);
                mContentView.setOnTouchListener(this);
                verseIndex++;
                if(verseIndex==verseTotal) {
                    //done with the list for today!
                    //popup and head back to home screen

                    finish();
                }
                screenIndex = 0;
                blankCard(todaysVerses);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        parent.removeView(temp);
                    }
                }, 500);
            }
            public void onSwipeRight() {
                if(screenIndex!=0) {
                    final ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_flip_left_out_back);
                    //parent.removeView(mContentView);
                    final View temp = mContentView;
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_subject, parent, false);
                    parent.addView(mContentView, index);
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_flip_left_in_back);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 0;
                    blankCard(todaysVerses);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            parent.removeView(temp);
                        }
                    }, 250);
                } else if (screenIndex == 0) {
                    final ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_flip_left_out_back);
                    final View temp = mContentView;
                    //parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_verse_full, parent, false);
                    parent.addView(mContentView, index);
                    PlayAnim(R.id.card_view_back, getBaseContext(), R.animator.card_flip_left_in_back);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 1;
                    backCard(todaysVerses);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            parent.removeView(temp);
                        }
                    }, 250);
                }
            }
            public void onSwipeLeft() {
                if(screenIndex!=1) {
                    final ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_flip_left_out);
                    final View temp = mContentView;
                    //parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_verse_full, parent, false);
                    parent.addView(mContentView, index);
                    PlayAnim(R.id.card_view_back, getBaseContext(), R.animator.card_flip_left_in);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 1;
                    backCard(todaysVerses);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            parent.removeView(temp);
                        }
                    }, 250);
                } else if( screenIndex == 1 ) {
                    final ViewGroup parent = (ViewGroup) mContentView.getParent();
                    int index = parent.indexOfChild(mContentView);
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_flip_left_out);
                    final View temp = mContentView;
                    //parent.removeView(mContentView);
                    mContentView = getLayoutInflater().inflate(R.layout.fragment_subject, parent, false);
                    parent.addView(mContentView, index);
                    PlayAnim(R.id.card_view_front, getBaseContext(), R.animator.card_flip_left_in);
                    mContentView.setOnTouchListener(this);
                    screenIndex = 0;
                    blankCard(todaysVerses);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            parent.removeView(temp);
                        }
                    }, 250);
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

    private void backCard(List<VerseCard> v) {

        View front = mContentView.findViewById(R.id.card_view_back);
        TextView preview = (TextView) front.findViewById(R.id.verse_preview_frag);

        preview.setText( v.get(verseIndex).getVerseStr());

        AutofitHelper verseAUTO = AutofitHelper.create(preview);
        verseAUTO.setMaxLines(8);
    }

    public void PlayAnim(int viewid, Context Con, int animationid )
    {
        View v = findViewById(viewid);

        if( v != null )
        {
            AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(Con, animationid  );
            animation.setTarget(v);
            animation.start();
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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verse_promote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Log.d("MENU", id + " : ");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_promote_verse) {

            int list = totalList.get(verseIndex).getList()+1;

            if(list <= 3) {
                totalList.get(verseIndex).promote(list);

                String listText = "";
                switch (list) {
                    case 0:
                        listText = "Daily";
                        break;
                    case 1:
                        listText = "Odd/Even";
                        break;
                    case 2:
                        listText = "Week";
                        break;
                    case 3:
                        listText = "Month";
                        break;
                    default:
                        break;
                }

                String date[] = totalList.get(verseIndex).getStartDate().split("/");
                int month = Integer.parseInt(date[0]);
                int day = Integer.parseInt(date[1]);
                int year = Integer.parseInt(date[2]);

                final Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month-1);
                c.set(Calendar.DATE, day);

                int DOW = c.get(Calendar.DAY_OF_WEEK);

                if(list == 1) {
                    totalList.get(verseIndex).setLevelInList(day%2);
                } else if ( list == 2) {
                    totalList.get(verseIndex).setLevelInList(DOW);
                } else if(list == 3) {
                    totalList.get(verseIndex).setLevelInList(day);
                }

                Toast.makeText(getBaseContext(), "Verse Promoted to the " + listText + " list", Toast.LENGTH_SHORT).show();
                Verses.saveVerses(getBaseContext());
            } else {
                Toast.makeText(getBaseContext(), "Already at max level", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
