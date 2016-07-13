package com.fibonaccistudios.nathanlea.versebox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class VersePicker extends AppCompatActivity {
    TextView chapter, book, verse, preview;
    int bookIndex=1, chapterNumber=1, startVerse=0, endVerse=0;

    View currentView;
    Bible bible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_picker);

        currentView = findViewById(R.id.relView);

        final Bible bible = new bibleVerseJSON(getApplicationContext()).getBible();
        this.bible = bible;
        final Context c = this;

        preview = (TextView) findViewById(R.id.textView8);

        book = (TextView) findViewById(R.id.textView2);
        assert book != null;
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(c, R.style.MaterialDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                            setGlobalBook(textview.getText()+"", Integer.parseInt(textview.getHint()+""));
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
                final Dialog dialog = new Dialog(c, R.style.MaterialDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_chapter);

                int bookNum = bookIndex;

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
                        textview.setText(((i * 5) + j - 5)+"");
                        if(bible.books.get(bookNum-1).chapter.size()+1 == ((i * 5) + j - 5)) {
                            finished = true;
                            break;
                        }
                        textview.setTextColor(Color.BLACK);
                        if(((i * 5) + j - 5)<10) {
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
                                    chapterNumber.get(i).setBackgroundResource(android.R.color.black);
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

        verse = (TextView) findViewById(R.id.textView6);
        assert verse != null;
        verse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(c, R.style.MaterialDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_verse);

                final ArrayList<TextView> verseNames = new ArrayList<>();
                int bookNum = bookIndex;
                int chapterNum = chapterNumber;

                boolean finished = false;
                final Stack<TextView> selectedQueue = new Stack<>();

                final TextView select = (TextView) dialog.findViewById(R.id.textView10);

                TableLayout tl = (TableLayout) dialog.findViewById(R.id.chapterTable);
                for (int i = 1; !finished; i++) {
                    TableRow tr1 = new TableRow(c);
                    tr1.setId(i + 100);
                    tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
                    for (int j = 1; j <= 5; j++) {
                        final TextView textview = new TextView(c);
                        textview.setId((j * j * 2) + 1);
                        textview.setText(((i * 5) + j - 5) + "");
                        textview.setTextColor(Color.BLACK);
                        if (bible.books.get(bookNum-1).chapter.get(chapterNum-1).getVerses()+1 == ((i * 5) + j - 5)) {
                            finished = true;
                            break;
                        }
                        if (((i * 5) + j - 5) < 10) {
                            textview.setPadding(60, 5, 60, 5);
                        } else
                            textview.setPadding(35, 5, 35, 5);
                        textview.setTextSize(40);
                        textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

                        textview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO: do your logic here
                                textview.setBackgroundResource(R.color.colorAccent);
                                selectedQueue.push(textview);
                                int tempVerse = Integer.parseInt(textview.getText()+"");
                                setVerseRange(tempVerse, tempVerse);

                                if(selectedQueue.size()>1) {
                                    //selectedQueue is now 2 or more
                                    //get the top two
                                    TextView a = selectedQueue.pop();
                                    TextView b = selectedQueue.pop();

                                    //get integers
                                    int aNum = Integer.parseInt(a.getText()+"");
                                    int bNum = Integer.parseInt(b.getText()+"");

                                    //Reset all the colors
                                    for(int i = 0; i < verseNames.size(); i++) {
                                        verseNames.get(i).setBackgroundResource(R.color.colorDialogMaterialWhite);
                                    }

                                    if(aNum > bNum) {
                                        for(int i = bNum-1; i < aNum; i++) {
                                            verseNames.get(i).setBackgroundResource(R.color.colorAccent);
                                        }
                                        setVerseRange(bNum, aNum);
                                    } else {
                                        for(int i = aNum-1; i < bNum; i++) {
                                            verseNames.get(i).setBackgroundResource(R.color.colorAccent);
                                        }
                                        setVerseRange(aNum, bNum);
                                    }
                                    selectedQueue.push(a);
                                }
                            }
                        });

                        select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel();
                            }
                        });


                        verseNames.add(textview);
                        tr1.addView(textview);
                    }
                    tl.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
                }
                dialog.show();
            }
        });
        updatePreviewBox();
    }

    public void setVerseRange(int start, int end) {
        if(start!=end) {
            verse.setText(start + " - " + end);
        } else {
            verse.setText(start+"");
        }
        startVerse = --start;
        endVerse = --end;
        updatePreviewBox();
    }

    public void setGlobalChapter(int i) {
        chapter.setText(i+"");
        chapterNumber = i;
        setVerseRange(1,1);
        updatePreviewBox();
    }

    public void setGlobalBook(String s, int index) {
        book.setText(s);
        bookIndex = index;

        //For now just clear the other settings if they had anything
        setGlobalChapter(1);
        setVerseRange(1,1);
        updatePreviewBox();
    }

    public void updatePreviewBox() {
        List<BibleXMLParser.Entry> entry = null;
        if(MainActivity.GloablBibleMap != null) {
            entry = MainActivity.GloablBibleMap.get(MainActivity.bookID.get(bookIndex - 1).ID + "." + chapterNumber);

            if (entry != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = startVerse; i <= endVerse; i++) {
                    sb.append(entry.get(i).text);
                    sb.append(" ");
                }
                preview.setText(sb.toString());
                return;
            }
        }
        new DownloadVerseTask().execute("");
    }

    private class DownloadVerseTask extends AsyncTask<String, Void, List<BibleXMLParser.Entry>> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected List<BibleXMLParser.Entry> doInBackground(String... urls) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // fetch data
                InputStream is = null;
                try {
                    URL url = new URL("https://en.bibles.org/v2/chapters/"+MainActivity.bookID.get(bookIndex-1).ID+"."+chapterNumber+"/verses.xml");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userCredentials = "lv1Ppb478BAoH56OFVNsv2kStGDe11kRy0vFeOFF:X";
                    String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
                    conn.setRequestProperty ("Authorization", basicAuth);
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setUseCaches(true);
                    conn.setDoOutput(true);

                    // Starts the query
                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d("DEBUG", "The response is: " + response);
                    is = conn.getInputStream();

                    try {
                        List<BibleXMLParser.Entry> entry = new BibleXMLParser().parse(is);
                        MainActivity.GloablBibleMap.put(MainActivity.bookID.get(bookIndex-1).ID+"."+chapterNumber, entry);

                        BufferedOutputStream out = null;
                        try {
                            out = new BufferedOutputStream(openFileOutput(MainActivity.BibleFileName, Context.MODE_PRIVATE));
                            ObjectOutputStream oos = new ObjectOutputStream(out);
                            oos.writeObject(MainActivity.GloablBibleMap);
                            oos.flush();
                            oos.close();
                        }
                        catch (Exception ex) {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (Exception e) {
                                    //assert true;
                                }
                            }
                        }

                        return entry;
                    } catch (Exception e) {
                        return null;
                    }

                    // Makes sure that the InputStream is closed after the app is
                    // finished using it.
                } catch (IOException e ) {
                    Log.e("IOEXCEPTION", e.toString());
                    return null;
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {

                        }
                    }
                }
            } else {
                // display error
                return null;
            }
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(List<BibleXMLParser.Entry> result) {
            //return result;
            if(result == null) {
                Snackbar snackbar = Snackbar
                        .make(currentView, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new DownloadVerseTask().execute("");
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                StringBuilder sb = new StringBuilder();
                for(int i = startVerse; i <= endVerse; i++) {
                    sb.append(result.get(i).text);
                    sb.append(" ");
                }
                preview.setText(sb.toString());
            }
        }
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

        //Log.d("MENU", id + " : ");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            Intent i = new Intent(this, VerseOptions.class);
            i.putExtra("bookIndex", bookIndex);
            i.putExtra("chapterNumber", chapterNumber);
            i.putExtra("startVerse", startVerse);
            i.putExtra("endVerse", endVerse);
            startActivityForResult(i, RESULT_CANCELED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_CANCELED) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
            }
            else if (resultCode == RESULT_CANCELED) {
                //We are coming back here set the correct vars
                bookIndex = (int)data.getExtras().get("bookIndex");
                chapterNumber = (int)data.getExtras().get("chapterNumber");
                startVerse = (int)data.getExtras().get("startVerse");
                endVerse = (int)data.getExtras().get("endVerse");
            }
        }
    }
}