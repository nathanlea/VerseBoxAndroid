package com.fibonaccistudios.nathanlea.versebox;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static List<BooksIDXMLParser.Book> bookID;
    static Map<String, List<BibleXMLParser.Entry>> GloablBibleMap;
    static List<VerseCard> verseList = new ArrayList<>();
    public final String BookIDFileName = "BOOKSID";
    public final static String BibleFileName = "Bible.bible";
    private TextView temp;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

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
                }
            });
        }
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

        //temp = (TextView) findViewById(R.id.tempVerseNum);
        //temp.setText("Number: " + verseList.size());

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mPlanetTitles = getResources().getStringArray(R.array.home_nav_bar);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close)  /* "close drawer" description for accessibility */
        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Verses.loadVerses(this);
        selectItem(0);

        //temp.setText("Number: " + verseList.size());
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
                            //assert true;
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
                //Save result
                bookID = result;
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(openFileOutput(BookIDFileName, Context.MODE_PRIVATE));
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(bookID);
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

            }
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_content_view, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
   public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        static  View secondView;

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if(getArguments().getInt(ARG_PLANET_NUMBER) == 0) {

                rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
                TextView temp = (TextView) rootView.findViewById(R.id.tempVerseNum);
                Button button = (Button) rootView.findViewById(R.id.todays_verses_button);
                temp.setText("Number: " + verseList.size());
                getActivity().setTitle("Home");

                FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
                if (fab != null) {
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getContext(), VersePicker.class);
                            startActivity(i);
                        }
                    });
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getContext(), VerseMemory.class);
                        startActivity(i);
                    }
                });
            } else if( getArguments().getInt(ARG_PLANET_NUMBER) == 1 ){
                if(secondView != null) {
                    return secondView;
                } else {
                    secondView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
                    RecyclerView rv = (RecyclerView) secondView.findViewById(R.id.rv);

                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    rv.setLayoutManager(llm);
                    rv.setHasFixedSize(true);

                    VerseAdapter adapter = new VerseAdapter(verseList);
                    adapter.setContext(getContext());
                    rv.setAdapter(adapter);
                    adapter.progressDialog = new ProgressDialog(getContext());
                    adapter.progressDialog.setMax(100);
                    adapter.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    adapter.progressDialog.setMessage("Loading All Verses");
                    adapter.progressDialog.show();
                    return secondView;
                }
            } else {
                rootView = inflater.inflate(R.layout.fragment_planet, container, false);
                int i = getArguments().getInt(ARG_PLANET_NUMBER);
                String planet = getResources().getStringArray(R.array.home_nav_bar)[i];

                int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                        "drawable", getActivity().getPackageName());
                ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
                getActivity().setTitle(planet);
            }
            return rootView;
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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
