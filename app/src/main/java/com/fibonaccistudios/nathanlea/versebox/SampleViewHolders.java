package com.fibonaccistudios.nathanlea.versebox;

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

    public SampleViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        bookName = (TextView) itemView.findViewById(R.id.gridViewTV);

        //Adjust this text size for the amount of times its mentioned
    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(),
                "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
                .show();
    }
}
