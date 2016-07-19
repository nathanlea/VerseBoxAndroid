package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.graphics.Palette;

/**
 * Created by Nathan on 7/13/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater=null;

    public ImageAdapter(Context c) {
        mContext = c;
        inflater = ( LayoutInflater )c.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.content_grid_card, null);
        holder.tv=(TextView) rowView.findViewById(R.id.cardContent);
        holder.img=(ImageView) rowView.findViewById(R.id.overlayImage);

        holder.tv.setText(mTitles[position]);
        holder.img.setImageResource(mThumbIds[position]);
        holder.img = new ImageView(mContext);
        holder.img.setLayoutParams(new GridView.LayoutParams(150, 150));
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.img.setPadding(8, 8, 8, 8);

        holder.img.setImageResource(mThumbIds[position]);

        final TextView tv = holder.tv;

        final int pos = position;
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (pos) {
                    case 0:
                        Intent i = new Intent(mContext, VersePicker.class);
                        mContext.startActivity(i);
                        break;
                    case 1:
                        i = new Intent(mContext, VerseMemory.class);
                        mContext.startActivity(i);
                        break;
                    case 2:
                        i = new Intent(mContext, allVerse.class);
                        mContext.startActivity(i);
                        break;
                    case 4:
                        i = new Intent(mContext, Topic.class);
                        mContext.startActivity(i);
                        break;
                    case 5:
                        i = new Intent(mContext, Section.class);
                        mContext.startActivity(i);
                        break;
                }

            }

        });

        return rowView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.add,R.drawable.daily,
            R.drawable.all, R.drawable.random,
            R.drawable.topic, R.drawable.sections
    };
    private String[] mTitles = {
            "New Verse", "Review Daily", "All Verses", "Random Verses", "Verses Topic", "Verse Sections"
    };
}