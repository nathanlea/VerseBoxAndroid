package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nathan on 7/18/2016.
 */
public class SampleViewHolders extends RecyclerView.ViewHolder implements
        View.OnClickListener
{
    public TextView bookName;
    public int ToS;
    public VerseCard verseCard = null;
    public View itemView;
    private Context c;
    private Topic topic;
    private Section section;

    public SampleViewHolders(View itemView, int ToS, Context c, Topic t)
    {
        super(itemView);
        this.c = c;
        itemView.setOnClickListener(this);
        bookName = (TextView) itemView.findViewById(R.id.gridViewTV);
        this.ToS = ToS;
        this.itemView = itemView;
        this.topic = t;

        //Adjust this text size for the amount of times its mentioned
    }

    public SampleViewHolders(View itemView, int ToS, Context c, Section s)
    {
        super(itemView);
        this.c = c;
        itemView.setOnClickListener(this);
        bookName = (TextView) itemView.findViewById(R.id.gridViewTV);
        this.ToS = ToS;
        this.itemView = itemView;
        this.section = s;
        //Adjust this text size for the amount of times its mentioned
    }

    public void setVerseCard(VerseCard vc) {
        this.verseCard = vc;
    }

    @Override
    public void onClick(View view)
    {
        TextView tv = (TextView) view.findViewById(R.id.gridViewTV);

        Intent i = new Intent(c, sectionListView.class);
        i.putExtra("section", tv.getText().toString());
        i.putExtra("TOS", ToS);
        if(ToS == 0) {
            topic.startActivityForResult(i, Topic.DATA_SET_CHANGED);
        } else {
            section.startActivityForResult(i, Section.DATA_SET_CHANGED);
        }
    }
}
