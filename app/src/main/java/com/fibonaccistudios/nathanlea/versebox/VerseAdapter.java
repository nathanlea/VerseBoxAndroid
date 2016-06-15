package com.fibonaccistudios.nathanlea.versebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import me.grantland.widget.AutofitHelper;

/**
 * Created by nathan on 6/15/16.
 */
public class VerseAdapter extends RecyclerView.Adapter<VerseAdapter.VerseViewHolder> {

    public static class VerseViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView verseRef, topic, section, sDate, eDate;


        VerseViewHolder(View itemView, final Context context) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view_front);
            verseRef = (TextView)itemView.findViewById(R.id.verseRefFrag);
            topic = (TextView)itemView.findViewById(R.id.topicFrag);
            section = (TextView)itemView.findViewById(R.id.sectionFrag);
            sDate = (TextView)itemView.findViewById(R.id.startDateFrag);
            eDate = (TextView)itemView.findViewById(R.id.endDateFrag);

            AutofitHelper verseAUTO = AutofitHelper.create(verseRef);
            verseAUTO.setMaxLines(1);

            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu pm = new PopupMenu(context, v);
                    pm.inflate(R.menu.card_view_menu);
                    pm.show();
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Edit")) {
                                //TODO Edit
                                //Go to edit page
                                //TODO
                            } else if (item.getTitle().equals("Delete")) {
                                //TODO Delete
                                //Confirm they want to delete then
                                // delete from master list and save list
                            } else {
                                //TBD
                            }
                            return false;
                        }
                    });
                    return false;
                }
            });
        }
    }

    List<VerseCard> verseCards;
    Context c;
    ProgressDialog progressDialog;

    VerseAdapter(List<VerseCard> verseCards){
        this.verseCards = verseCards;
    }

    public void setContext(Context c) {
        this.c = c;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public VerseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragement_verse_front, viewGroup, false);
        VerseViewHolder vvh = new VerseViewHolder(v, c);
        //new addViewToPage().execute(vvh, verseCards, i, c);
        return vvh;
    }

    @Override
    public void onBindViewHolder(VerseViewHolder verseViewHolder, int i) {
        new addViewToPage().execute(verseViewHolder, verseCards, i, c);
    }

    @Override
    public int getItemCount() {
        return verseCards.size();
    }

    private class addViewToPage extends AsyncTask<Object, Void, Void> {
        VerseViewHolder vvh;
        List<VerseCard> vc;
        int i;
        String[] sb = new String[5];

        @Override
        protected Void doInBackground(Object... params) {
            vvh = (VerseViewHolder)params[0];
            vc = (List<VerseCard>)params[1];
            i = (Integer) params[2];
            Context c = (Context) params[3];

            vc.get(i).setAppContext(c);
            vc.get(i).buildVerseCardStrings();
            sb[0] = vc.get(i).getVerseReference();
            sb[1] = vc.get(i).getTopic();
            sb[2] = vc.get(i).getSection();
            sb[3] = vc.get(i).getStartDate();
            sb[4] = vc.get(i).getEndDate();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            vvh.verseRef.setText(sb[0]);
            vvh.topic.setText(sb[1]);
            vvh.section.setText(sb[2]);
            vvh.sDate.setText(sb[3]);
            vvh.eDate.setText(sb[4]);
            if(i+1 == getItemCount()) {
                progressDialog.cancel();
            }
        }
    }
}
