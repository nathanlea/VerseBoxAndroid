package com.fibonaccistudios.nathanlea.versebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Topic extends AppCompatActivity {

    static final int DATA_SET_CHANGED = 1;

    private StaggeredGridLayoutManager _sGridLayoutManager;
    private Map<String, Integer> map = new HashMap<>();
    private GridAdapter rcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        _sGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        _sGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        //StaggeredGridLayoutManager.LayoutParams layoutParams =
        //        new StaggeredGridLayoutManager.LayoutParams(
         //               ViewGroup.LayoutParams.MATCH_PARENT,
         //               ViewGroup.LayoutParams.MATCH_PARENT);

        //layoutParams.setFullSpan(true);

        //_sGridLayoutManager.generateLayoutParams(layoutParams);

        recyclerView.setLayoutManager(_sGridLayoutManager);

        map = VerseCardSort.buildTextSizeTopic();

        rcAdapter = new GridAdapter(
                this, map, 0, this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DATA_SET_CHANGED) {
            rcAdapter.notifyDataSetChanged();
        }
    }
}
