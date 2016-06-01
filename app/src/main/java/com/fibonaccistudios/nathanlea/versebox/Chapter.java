package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Chapter.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Chapter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chapter extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<TextView> chapterNumber = new ArrayList<>();

    private Context c;
    private Bible bible;

    private OnFragmentInteractionListener mListener;

    public Chapter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Verse.
     */
    // TODO: Rename and change types and number of parameters
    public static Chapter newInstance(String param1, String param2) {
        Chapter fragment = new Chapter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public void setBible( Bible b ) { this.bible = b;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chapter, container, false);

        int bookNum = ((VerseSelector)c).getCurrentBook();

        Log.d("BOOK", "B: "+bookNum);

        TableLayout tl = (TableLayout) v.findViewById(R.id.chapterTable);
        boolean finished = false;

        for(int i = 1; !finished; i++) {
            TableRow tr1 = new TableRow(getC());
            tr1.setId(i*101);
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            for(int j = 1; j <= 5; j++) {
                final TextView textview = new TextView(getC());
                textview.setId(((j*j*2)+1)*73);
                textview.setText(((i*4)+j-4)+"");
                bookNum = ((VerseSelector)c).getCurrentBook();
                if(bible.books.get(bookNum).chapter.size() == (i*4)+j-4) {
                    finished = true;
                    break;
                }
                textview.setTextColor(Color.BLACK);
                if(((i*4)+j-4)<10) {
                    textview.setPadding(105,5,105,5);
                } else
                    textview.setPadding(75,5,75,5);
                textview.setTextSize(40);
                textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

                textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: do your logic here
                        for(int i = 0; i < chapterNumber.size(); i++) {
                            chapterNumber.get(i).setBackgroundResource(android.R.color.white);
                        }
                        ((VerseSelector)c).setCurrentChapter(Integer.parseInt(textview.getText()+""));
                        textview.setBackgroundResource(R.color.colorAccent);
                        ((VerseSelector)c).mViewPager.setCurrentItem(2, true);
                    }
                });
                chapterNumber.add(textview);
                tr1.addView(textview);
            }
            tl.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
        }
        Snackbar.make(v, "Created Table View", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
