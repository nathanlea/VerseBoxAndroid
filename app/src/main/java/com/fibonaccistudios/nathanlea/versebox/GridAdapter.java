package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitHelper;

/**
 * Created by Nathan on 7/18/2016.
 */

public class GridAdapter extends RecyclerView.Adapter<SampleViewHolders>
{
    private Context context;
    private int ToS;
    Map<String, Integer> map = new HashMap<>();
    ArrayList<String> titles = new ArrayList<>();
    private Section section;
    private Topic topic;

    public GridAdapter(Context context, Map<String, Integer> itemList, int TopicOrSection, Section s)
    {
        this.map = itemList;
        this.context = context;
        this.ToS = TopicOrSection;
        this.section = s;

        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            titles.add(entry.getKey());
        }
    }

    public GridAdapter(Context context, Map<String, Integer> itemList, int TopicOrSection, Topic t)
    {
        this.map = itemList;
        this.context = context;
        this.ToS = TopicOrSection;
        this.topic = t;

        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            titles.add(entry.getKey());
        }
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_grid_view, null);
        SampleViewHolders rcv;
        if(ToS==0)
            rcv = new SampleViewHolders(layoutView, ToS, context, topic);
        else
            rcv = new SampleViewHolders(layoutView, ToS, context, section);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position)
    {
        holder.bookName.setText(titles.get(position));
        holder.bookName.setTextSize(map.get(titles.get(position)));

        AutofitHelper verseAUTO = AutofitHelper.create(holder.bookName);
        verseAUTO.setMaxLines(1);
    }

    @Override
    public int getItemCount()
    {
        return this.map.size();
    }

}

