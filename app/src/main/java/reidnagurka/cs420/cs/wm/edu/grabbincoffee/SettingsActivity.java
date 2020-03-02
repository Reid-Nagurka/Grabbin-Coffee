package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * Used to set the dark mode, if you want
 * ALSO, unlink calendar.
 *
 * In this push, greg is using the buttons to make get and post requests to an external API he built
 */
public class SettingsActivity extends AppCompatActivity {

    // ANDROID STUDIO STUFF
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3

    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.v("Calendar Contract", EVENT_PROJECTION[0]);
        putContentValues();



        configurePostUpButtion();
    }

    private void putContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "greg.garnhart12@gmail.com");
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.google");
        contentValues.put(CalendarContract.Calendars.NAME, "greg.garnhart12@gmail.com");
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, "greg.garnhart12@gmail.com");

        // event instance things (basically from week to week)
        Date today = new Date();
        contentValues.put(CalendarContract.Instances.DTSTART, today.getTime());
        contentValues.put(CalendarContract.Instances.DTEND, (today.getTime() + 604800000L));
    }

    private void configurePostUpButtion(){
        Button postUpButton = findViewById(R.id.postUp);
        final CheckBox monday = findViewById(R.id.checkBoxMonday);
        final CheckBox tuesday = findViewById(R.id.checkBoxTuesday);

        postUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(monday.isChecked()){
//
//                }
                Log.v("BUTTON ACTION", "Looking for calendar invites");
                runCalendarQuery();
            }
        });
    }

    private void runCalendarQuery(){

        if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            Log.v("PERMISSION", "bad news. calendar reading is not valid.");
        }
        else{
            // we have permission to read the calendar.
            Log.v("Permission granted", "good news! We can read the calendar");
        }


        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

//        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + ") AND ("
//                + CalendarContract.Calendars.ACCOUNT_TYPE + ") AND ("
//                + CalendarContract.Calendars.OWNER_ACCOUNT + "))";

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.DURATION,
                        CalendarContract.Events.AVAILABILITY
                };
//        String eventsSelection = "(("+CalendarContract.Calendars._ID+") AND ("+CalendarContract.Instances.DTSTART+") AND ("+CalendarContract.Instances.DTEND+"))";


        String eventsSelection = "(" + CalendarContract.Calendars._ID +")";


//        String [] selectionArgs = new String[]{String.valueOf(today.getTime()), String.valueOf(today.getTime() + 604800000L)};
        cur = cr.query(uri, mProjection, eventsSelection, null, CalendarContract.Instances.DTSTART + " ASC");


        // Hey! We got this week's events
        HashMap<Long, Long> weeklyEvents = getThisWeeksEvents(cur);
    }

    private HashMap getThisWeeksEvents(Cursor cur){
        // Use the cursor to step through the returned records
        Date today = new Date();
        Long start_week = today.getTime();
        Long end_week = start_week + 604800000L;
        HashMap<Long, Long> weeklyEvents = new HashMap<Long,Long>();

        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            String start = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            String end = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTEND));
            String duration = cur.getString(cur.getColumnIndex(CalendarContract.Events.DURATION));
            String availability = cur.getString(cur.getColumnIndex(CalendarContract.Events.AVAILABILITY));

            Long event_start = Long.parseLong(start);
            Long event_end = 0L;
            Boolean ignore = false;
            if(end != null){
                event_end = Long.parseLong(end);
            }
            else{
                if (duration.equals("P1D") && availability.equals("0")){
                    event_end = event_start + TimeUnit.DAYS.toMillis(1); // all day events where they are busy.
                }
                else if (duration.equals("P1D") && availability.equals("1")){
                    event_end = event_start; // all day event, but you're available for the duration of the event. (e.g. birthdays)
                }
                else {
                    event_end = event_start + TimeUnit.HOURS.toMillis(1); // catch all: just make it an hour long.
                }
            }

            if(event_start > start_week && event_end < end_week){
                weeklyEvents.put(event_start, event_end);
            }


            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            // Do something with the values...
        }

        return weeklyEvents;
    }

}
