package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<BooksIDXMLParser.Book> bookID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getBaseContext(), VersePicker.class);
                    startActivity(i);

                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        new DownloadBooksID().execute("");
    }


    private class DownloadBooksID extends AsyncTask<String, Void, List<BooksIDXMLParser.Book>> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
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
                        return new BooksIDXMLParser().parse(is);
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
        protected void onPostExecute(List<BooksIDXMLParser.Book> result) {
            //return result;
            if(result == null) {
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
                bookID = result;
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
