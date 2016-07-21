package com.fibonaccistudios.nathanlea.versebox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;

public class sectionListView extends AppCompatActivity {

    RecyclerView recyclerView;
    AllVerseAdapter adapter;
    SwipeToAction swipeToAction;
    static public VerseCard editingVerse = null;

    List<VerseCard> verseCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO maybe sort these by something? Gen-Rev?

        String section = (String) getIntent().getExtras().get("section");
        int ToS = (int) getIntent().getExtras().get("TOS");

        if(ToS==0) {
            verseCards = VerseCardSort.orderByTopics().get(section);
        } else {
            verseCards = VerseCardSort.orderBySections().get(section);
        }

        setTitle(section);

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
                final VerseCard tempVerse = itemData;
                if(!tempVerse.isLoved()) {
                    tempVerse.setLoved(true);
                    displaySnackbar(itemData.getVerseReference() + " loved", "Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tempVerse.setLoved(false);
                            Verses.saveVerses(getBaseContext());
                        }
                    });
                    Verses.saveVerses(getBaseContext());
                    adapter.notifyDataSetChanged();
                } else {
                        tempVerse.setLoved(false);
                        displaySnackbar(itemData.getVerseReference() + " unloved", "Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tempVerse.setLoved(true);
                            }
                        });
                    Verses.saveVerses(getBaseContext());
                    adapter.notifyDataSetChanged();
                }

                return true;
            }

            @Override
            public void onClick(VerseCard itemData) {
                //TODO view the verse card here
                // Allow to edit also!
                //displaySnackbar(itemData.getVerseReference() + " clicked", null, null);

                editingVerse = itemData;

                Intent i = new Intent(getBaseContext(), VerseOptions.class);

                i.putExtra("bookIndex", itemData.getBookIndex());
                i.putExtra("chapterNumber", itemData.getChapter());
                i.putExtra("startVerse", itemData.getStartVerse());
                i.putExtra("endVerse", itemData.getEndVerse());

                i.putExtra("editing", true);

                i.putExtra("verseREF", itemData.getVerseReference());
                i.putExtra("preview", itemData.getVerseStr());
                i.putExtra("topic", itemData.getTopic());
                i.putExtra("section", itemData.getSection());
                i.putExtra("startdate", itemData.getStartDate());
                i.putExtra("enddate", itemData.getEndDate());
                i.putExtra("position", MainActivity.verseList.indexOf(itemData));

                startActivityForResult(i, RESULT_CANCELED);
            }

            @Override
            public void onLongClick(VerseCard itemData) {}
        });
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
        MainActivity.verseList.remove(verse);
        Verses.saveVerses(this);
        return pos;
    }

    private void addVerse(int pos, VerseCard verse) {
        verseCards.add(pos, verse);
        adapter.notifyItemInserted(pos);
        MainActivity.verseList.add(pos, verse);
        Verses.saveVerses(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}