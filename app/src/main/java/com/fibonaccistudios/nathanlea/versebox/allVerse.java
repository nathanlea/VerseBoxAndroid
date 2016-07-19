package com.fibonaccistudios.nathanlea.versebox;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;

public class allVerse extends AppCompatActivity {

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_recycler_view);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        VerseAdapter adapter = new VerseAdapter(MainActivity.verseList);
        adapter.setContext(this);
        rv.setAdapter(adapter);
    }*/

    RecyclerView recyclerView;
    AllVerseAdapter adapter;
    SwipeToAction swipeToAction;

    List<VerseCard> verseCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO maybe sort these by something? Gen-Rev?

        verseCards = MainActivity.verseList;

        // facebook image library
        Fresco.initialize(this);

        setContentView(R.layout.activity_all_verses);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AllVerseAdapter(this.verseCards);
        recyclerView.setAdapter(adapter);

        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<VerseCard>() {
            @Override
            public boolean swipeLeft(final VerseCard itemData) {
                final int pos = removeVerse(itemData);
                displaySnackbar(itemData.getVerseReference() + " removed", "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addVerse(pos, itemData);
                    }
                });
                return true;
            }

            @Override
            public boolean swipeRight(VerseCard itemData) {
                displaySnackbar(itemData.getVerseReference() + " loved", null, null);
                return true;
            }

            @Override
            public void onClick(VerseCard itemData) {
                displaySnackbar(itemData.getVerseReference() + " clicked", null, null);
            }

            @Override
            public void onLongClick(VerseCard itemData) {
                displaySnackbar(itemData.getVerseReference() + " long clicked", null, null);
            }
        });

        // use swipeLeft or swipeRight and the elem position to swipe by code
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToAction.swipeRight(2);
            }
        }, 3000);*/
    }
    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        View r = findViewById(R.id.snackbarPosition);
        Snackbar snack = Snackbar.make(r, text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLACK);

        snack.show();
    }

    private int removeVerse(VerseCard verse) {
        int pos = verseCards.indexOf(verse);
        verseCards.remove(verse);
        adapter.notifyItemRemoved(pos);
        MainActivity.verseList = verseCards;
        Verses.saveVerses(this);
        return pos;
    }

    private void addVerse(int pos, VerseCard verse) {
        verseCards.add(pos, verse);
        adapter.notifyItemInserted(pos);
        MainActivity.verseList = verseCards;
        Verses.saveVerses(this);
    }
}
