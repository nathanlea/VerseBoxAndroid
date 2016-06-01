package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ngoalie on 5/31/2016.
 */
public class bibleVerseJSON {

    private Bible bible;

    public bibleVerseJSON( Context c ) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(c.getAssets().open("bible.json")));

            // do reading, usually loop until end of file reading
            String mLine;
            StringBuilder sb = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                //process line
                sb.append(mLine);
            }
            toJSON(sb.toString());
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    private void toJSON( String s ) {
        Bible b = new BibleJSONParser(s).getBible();
        bible = b;
    }

    public Bible getBible() {
        return bible;
    }
}
