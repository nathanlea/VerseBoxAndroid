package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static List<BooksIDXMLParser.Book> bookID;
    static Map<String, List<BibleXMLParser.Entry>> GloablBibleMap;
    static List<VerseCard> verseList = new ArrayList<>();
    static Bible bible;
    public final String BookIDFileName = "BOOKSID";
    public final static String BibleFileName = "Bible.bible";
    private TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_list_main);
        setTitle("Verse Box");

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MainActivityAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        File file = new File(getFilesDir() + "/" + BookIDFileName);
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
            bookID = (List<BooksIDXMLParser.Book>) oin.readObject();
            oin.close();
            in.close();
            if(bookID == null) {
                new DownloadBooksID().execute("");
            }
        } catch (Exception e) {
            new DownloadBooksID().execute("");
        } finally {
            try {
                if(in != null)
                    in.close();
            } catch (Exception e) {}
        }

        file = new File(getFilesDir() + "/" + BibleFileName);
        in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
            GloablBibleMap = (Map<String, List<BibleXMLParser.Entry>>) oin.readObject();
            oin.close();
            in.close();
        } catch (Exception e) {
            GloablBibleMap = new HashMap<>();
        } finally {
            try {
                if(in != null)
                    in.close();
            } catch (Exception e) {}
        }

        Verses.loadVerses(this);

        bible = new bibleVerseJSON(this).getBible();

    }

    protected void onStop() {
        super.onStop();
        Verses.saveVerses(this);
    }

    protected void onResume() {
        super.onResume();
        Verses.loadVerses(this);
    }

    private class DownloadBooksID extends AsyncTask<String, Void, List<BooksIDXMLParser.Book>> {
        /**
         * The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()
         */
        protected List<BooksIDXMLParser.Book> doInBackground(String... urls) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // fetch data
                InputStream is = null;
                try {
                    URL url = new URL("https://bibles.org/v2/versions/eng-ESV/books.xml");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String userCredentials = "lv1Ppb478BAoH56OFVNsv2kStGDe11kRy0vFeOFF:X";
                    String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));
                    conn.setRequestProperty("Authorization", basicAuth);
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
                        return new BooksIDXMLParser().parse(is);
                    } catch (Exception e) {
                        return null;
                    }

                    // Makes sure that the InputStream is closed after the app is
                    // finished using it.
                } catch (IOException e) {
                    Log.e("IOEXCEPTION", e.toString());
                    return null;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            //assert true;
                        }
                    }
                }
            } else {
                // display error
                return null;
            }
        }

        /**
         * The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
         */
        protected void onPostExecute(List<BooksIDXMLParser.Book> result) {
            //return result;
            if (result == null) {
                /*Snackbar snackbar = Snackbar
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
                snackbar.show();*/
            } else {
                //Save result
                bookID = result;
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(openFileOutput(BookIDFileName, Context.MODE_PRIVATE));
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(bookID);
                    oos.flush();
                    oos.close();
                } catch (Exception ex) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                            //assert true;
                        }
                    }
                }

            }
        }
    }
}
