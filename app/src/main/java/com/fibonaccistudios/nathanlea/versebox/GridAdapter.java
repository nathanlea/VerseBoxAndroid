package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Nathan on 7/18/2016.
 */


public class GridAdapter extends RecyclerView.Adapter<SampleViewHolders>
{
    private List<VerseGridItem> itemList;
    private Context context;

    public GridAdapter(Context context, List<VerseGridItem> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_grid_view, null);
        SampleViewHolders rcv = new SampleViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position)
    {
        holder.bookName.setText(itemList.get(position).title);
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }
}

