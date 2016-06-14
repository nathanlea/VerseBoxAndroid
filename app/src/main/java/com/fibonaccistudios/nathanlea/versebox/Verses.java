package com.fibonaccistudios.nathanlea.versebox;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Verses {

    final static String verseFileName = "VERSE.v";

    public static void loadVerses( Context c ) {
        File file = new File(c.getFilesDir() + "/" + verseFileName);
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
            MainActivity.verseList = (List<VerseCard>) oin.readObject();
            oin.close();
            in.close();
        } catch (Exception e) {
            //Guess it isn't here!
            e.printStackTrace();
        } finally {
            try {
                if(in != null)
                    in.close();
            } catch (Exception e) {}
        }
    }

    public static void saveVerses( Context c ) {

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(c.openFileOutput(verseFileName, Context.MODE_PRIVATE));
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(MainActivity.verseList);
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
