package com.fibonaccistudios.nathanlea.versebox;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import me.grantland.widget.AutofitHelper;

public class VerseOptions extends AppCompatActivity {

    int bookIndex=1, chapterNumber=1, startVerse=1, endVerse=1;
    TextView verseREF = null;
    EditText preview = null;
    EditText topic = null;
    EditText section = null;
    TextView start_date = null;
    TextView end_date = null;

    String verseREF_s  = null;
    String verseSTR_s  = null;
    String topic_s     = null;
    String section_s   = null;
    String startdate_s = null;
    String endDate_s   = null;
    boolean editingVerse = false;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_options);

        bookIndex = (int)getIntent().getExtras().get("bookIndex");
        chapterNumber = (int)getIntent().getExtras().get("chapterNumber");
        startVerse = (int)getIntent().getExtras().get("startVerse");
        endVerse = (int)getIntent().getExtras().get("endVerse");
        setTitle("Finalize Verse Card");

        editingVerse = (boolean)getIntent().getExtras().get("editing");
        if(editingVerse) {
            verseREF_s  = (String)getIntent().getExtras().get("verseREF");
            verseSTR_s  = (String)getIntent().getExtras().get("preview");
            topic_s     = (String)getIntent().getExtras().get("topic");
            section_s   = (String)getIntent().getExtras().get("section");
            startdate_s = (String)getIntent().getExtras().get("startdate");
            endDate_s   = (String)getIntent().getExtras().get("enddate");
            position = (int)getIntent().getExtras().get("position");
            setTitle("Edit Verse Card");
        }

        verseREF = (TextView) findViewById(R.id.verseRef_TV);
        start_date = (TextView) findViewById(R.id.startDate_TV);
        end_date = (TextView) findViewById(R.id.endDate_TV);

        preview = (EditText) findViewById(R.id.verse_preview_tv);
        topic = (EditText)findViewById(R.id.topic_TV);
        section = (EditText)findViewById(R.id.section_TV);


        AutofitHelper verseAUTO = AutofitHelper.create(verseREF);
        verseAUTO.setMaxLines(1);
        if(!editingVerse) {
            //TODO Make this not have to do this much work every time
            final Bible bible = new bibleVerseJSON(getApplicationContext()).getBible();
            int tempStart = startVerse + 1;
            int tempEnd = endVerse + 1;
            String verse;
            if (startVerse == endVerse) {
                verse = bible.books.get(bookIndex - 1).getTitle() + " " + chapterNumber + " : " + tempStart;
            } else {
                verse = bible.books.get(bookIndex - 1).getTitle() + " " + chapterNumber + " : " + tempStart + " - " + tempEnd;
            }

            if (verseREF != null)
                verseREF.setText(verse);

            List<BibleXMLParser.Entry> entry = MainActivity.GloablBibleMap.get(MainActivity.bookID.get(bookIndex - 1).ID + "." + chapterNumber);
            if (entry != null && preview != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = startVerse; i <= endVerse; i++) {
                    sb.append(entry.get(i).text);
                    sb.append(" ");
                }
                String clean = sb.toString().replaceAll("<span.*?>|</span>", "");
                preview.setText(clean);
            }

            final Calendar c = Calendar.getInstance();
            int year, month, day;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DATE);
            if (start_date != null && end_date != null) {
                start_date.setText(month + "/" + day + "/" + year);
                if (month == 11) {
                    month = 1;
                } else if (month == 12) {
                    month = 2;
                    if (VerseBoxCal.isLeapYear(year) && day > 29) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                } else {
                    month += 2;
                }
                end_date.setText(month + "/" + day + "/" + year);
            }
        } else {

            verseREF.setText(verseREF_s);
            preview.setText(verseSTR_s);
            topic.setText(topic_s);
            section.setText(section_s);
            start_date.setText(startdate_s);
            end_date.setText(endDate_s);

        }
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = start_date.getText().toString();
                String[] parts = date.split("/");
                int month = Integer.parseInt(parts[0]);
                int day = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                showTimePickerDialog(v, start_date, end_date, 0, day, month - 1, year);
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = end_date.getText().toString();
                String[] parts = date.split("/");
                int month = Integer.parseInt(parts[0]);
                int day = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                showTimePickerDialog(v, start_date, end_date, 1, day, month - 1, year);
            }
        });
    }

    public void showTimePickerDialog(View v, TextView tv, TextView tv2, int option, int day, int month, int year) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setStartDate(day, month, year);
        newFragment.setTextView(tv, tv2, option);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onBackPressed() {
        if(!editingVerse) {
            Intent i = new Intent(this, VersePicker.class);
            i.putExtra("bookIndex", bookIndex);
            i.putExtra("chapterNumber", chapterNumber);
            i.putExtra("startVerse", startVerse);
            i.putExtra("endVerse", endVerse);
            setResult(RESULT_CANCELED, i);
        } else {
            Intent i = new Intent(this, allVerse.class);
            setResult(RESULT_CANCELED, i);
        }
        super.onBackPressed();
    }


    public static class TimePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        TextView tv;
        TextView tv2;
        int year = 0, month = 0, day = 0, option = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            if(year == 0) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DATE);
            }

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setStartDate( int day, int month, int year ) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public void setTextView(TextView tv, TextView tv2, int option) {
            this.tv = tv;
            this.tv2 = tv2;
            this.option = option;
        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ++monthOfYear;
            if(option==0) {
                tv.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
                if (monthOfYear == 11) {
                    monthOfYear = 1;
                } else if (monthOfYear == 12) {
                    monthOfYear = 2;
                    if(VerseBoxCal.isLeapYear(year) && dayOfMonth > 29) {
                        dayOfMonth = 29;
                    } else {
                        dayOfMonth = 28;
                    }
                } else {
                    monthOfYear += 2;
                }
                tv2.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
            } else {
                tv2.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verse_options_menu, menu);
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
        if (id == R.id.action_finish) {

            //public VerseCard(int bookIndex, int chapter, int start_verse, int end_verse,
            // String startDate, String endDate,
            // String topic, String section)
            if(!editingVerse) {
                VerseCard v = new VerseCard(bookIndex, chapterNumber, startVerse, endVerse,
                        start_date.getText().toString(), end_date.getText().toString(),
                        preview.getText().toString(), topic.getText().toString(),
                        section.getText().toString());
                v.buildVerseCardStrings();
                MainActivity.verseList.add(v);
                Verses.saveVerses(this);
                setResult(RESULT_OK);
                finish();
            } else {
                Intent i = new Intent(this, allVerse.class);
                VerseCard vc = MainActivity.verseList.get(position);
                MainActivity.verseList.remove(position);
                vc.rebuildVerseCard(
                        start_date.getText().toString(),
                        end_date.getText().toString(),
                        verseREF.getText().toString(),
                        preview.getText().toString(),
                        topic.getText().toString(),
                        section.getText().toString());
                MainActivity.verseList.add(position, vc);

                Verses.saveVerses(getBaseContext());

                setResult(RESULT_OK, i);
                //onBackPressed();
                finish();
            }

            //setResult(RESULT_OK);
            //finish();

            //save Verse to folder

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}