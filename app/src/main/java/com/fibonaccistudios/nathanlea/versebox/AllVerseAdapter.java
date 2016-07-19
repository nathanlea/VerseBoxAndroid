package com.fibonaccistudios.nathanlea.versebox;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.dift.ui.SwipeToAction;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Nathan on 7/18/2016.
 */
public class AllVerseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VerseCard> items;


    /** References to the views for each data item **/
    public class AllVerseHolder extends SwipeToAction.ViewHolder<VerseCard> {
        public TextView titleView;
        public TextView authorView;
        public SimpleDraweeView imageView;

        public AllVerseHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.title);
            authorView = (TextView) v.findViewById(R.id.author);
            imageView = (SimpleDraweeView) v.findViewById(R.id.image);
        }
    }

    /** Constructor **/
    public AllVerseAdapter(List<VerseCard> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.verse_view, parent, false);

        return new AllVerseHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VerseCard item = items.get(position);
        AllVerseHolder vh = (AllVerseHolder) holder;
        vh.titleView.setText(item.getVerseReference());
        vh.authorView.setText(item.getVerseStr().substring(0,45)+"...");
        vh.data = item;
    }
}
