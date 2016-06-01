package com.fibonaccistudios.nathanlea.versebox;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VersePicker extends AppCompatActivity {
    TextView chapter, book, verse;
    int chapterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_picker);

        final Bible bible = new bibleVerseJSON(getApplicationContext()).getBible();
        final Context c = this;

        book = (TextView) findViewById(R.id.textView2);
        assert book != null;
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.fragment_books);

                TableLayout tl = (TableLayout) dialog.findViewById(R.id.bookTableView);
                for(int i = 1; i < 67; i++) {
                    TableRow tr1 = new TableRow(c);
                    tr1.setId(i*783985);
                    tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
                    final TextView textview = new TextView(c);
                    textview.setId(i + 0);
                    textview.setText(bible.books.get(i-1).getTitle());
                    textview.setTextColor(Color.BLACK);
                    textview.setPadding(100, 15, 0, 15);
                    textview.setTextSize(30);
                    textview.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    textview.setHint(i+"");

                    textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setGlobalBook(textview.getText()+"");
                            dialog.dismiss();
                        }
                    });
                    tr1.addView(textview);
                    tl.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
                }
                dialog.show();
            }
        });


        chapter = (TextView) findViewById(R.id.textView4);
        assert chapter != null;
        chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.fragment_chapter);
                dialog.setTitle("Title...");

                int bookNum = 1;

                Log.d("BOOK", "B: "+ bookNum);

                TableLayout tl = (TableLayout) dialog.findViewById(R.id.chapterTable);
                boolean finished = false;
                final ArrayList<TextView> chapterNumber = new ArrayList<>();

                for(int i = 1; !finished; i++) {
                    TableRow tr1 = new TableRow(c);
                    tr1.setId(i*101);
                    tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
                    for(int j = 1; j <= 5; j++) {
                        final TextView textview = new TextView(c);
                        textview.setId(((j*j*2)+1)*73);
                        textview.setText(((i*4)+j-4)+"");
                        if(bible.books.get(bookNum).chapter.size() == (i*4)+j-4) {
                            finished = true;
                            break;
                        }
                        textview.setTextColor(Color.BLACK);
                        if(((i*4)+j-4)<10) {
                            textview.setPadding(60,5,60,5);
                        } else
                            textview.setPadding(35,5,35,5);
                        textview.setTextSize(40);
                        textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

                        textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO: do your logic here
                                for(int i = 0; i < chapterNumber.size(); i++) {
                                    chapterNumber.get(i).setBackgroundResource(android.R.color.white);
                                }
                                textview.setBackgroundResource(R.color.colorAccent);
                                setGlobalChapter(Integer.parseInt(textview.getText()+""));
                                dialog.dismiss();
                            }
                        });
                        chapterNumber.add(textview);
                        tr1.addView(textview);
                    }
                    tl.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
                }
                dialog.show();
            }
        });
    }

    public void setGlobalChapter(int i) {
        chapter.setText(i+"");
        chapterNumber = i;
    }

    public void setGlobalBook(String s) {
        book.setText(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verse_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
