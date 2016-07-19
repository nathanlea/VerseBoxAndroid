package com.fibonaccistudios.nathanlea.versebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Topic extends AppCompatActivity {

    private StaggeredGridLayoutManager _sGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        _sGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        List<VerseGridItem> sList = getListItemData();

        GridAdapter rcAdapter = new GridAdapter(
                this, sList);
        recyclerView.setAdapter(rcAdapter);
    }

    private List<VerseGridItem> getListItemData()
    {
        List<VerseGridItem> listViewItems = new ArrayList<>();

        List<VerseCard> vc = MainActivity.verseList;
        for(int i = 0; i<vc.size(); i++) {
            listViewItems.add(new VerseGridItem(vc.get(i).getTopic()));
        }
        return listViewItems;
    }
}
