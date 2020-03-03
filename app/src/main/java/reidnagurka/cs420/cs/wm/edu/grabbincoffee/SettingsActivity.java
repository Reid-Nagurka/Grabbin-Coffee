package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
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
                // A HashMap of our selected weekdays.
                // Important to note that the capitilization is important!
                // It is compared to a string returned from a calendar function
                final HashMap<String, Boolean> selectedWeekdays = new HashMap<String, Boolean>();
                selectedWeekdays.put("Monday", monday.isChecked());
                selectedWeekdays.put("Tuesday", tuesday.isChecked());
                selectedWeekdays.put("Wednesday", tuesday.isChecked());
                selectedWeekdays.put("Thursday", tuesday.isChecked());
                selectedWeekdays.put("Friday", tuesday.isChecked());
                selectedWeekdays.put("Saturday", tuesday.isChecked());
                selectedWeekdays.put("Sunday", tuesday.isChecked());
                CoffeeEvent[] proposedEvents = runCalendarQuery(selectedWeekdays);
            }
        });
    }

    private CoffeeEvent[] runCalendarQuery(HashMap<String,Boolean> selectedWeekdays){

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
        HashMap<Long, Long> conflictingWeeklyEvents = getThisWeeksEvents(cur, selectedWeekdays);

        // create a list of possible events!
        HashMap proposals = getCoffeeProposals(conflictingWeeklyEvents, selectedWeekdays);
        CoffeeEvent [] coffeeEvents = getCoffeeEvents(proposals);
        Arrays.sort(coffeeEvents);
        return coffeeEvents;

    }

    /**
     * Loops through a calendar of events for the coming week based on the current time.
     * (e.g. if today is Tuesday, then it will go from Tuesday - Monday)
     *
     * @param cur is the cursor that points to a table of events from a calendar.
     * @return a HashMap of conflicting events for the Selected Days of the Week
     */
    private HashMap getThisWeeksEvents(Cursor cur, HashMap<String, Boolean> selectedWeekdays){
        // Use the cursor to step through the returned records

        // Initialize a calendar date object for TODAY's midnight time (today at 12:00:00:0000 AM)
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Long start_week = today.getTimeInMillis();

        // Now, set the end week variable to today + a week
        today.add(Calendar.DAY_OF_WEEK,7);
        Long end_week = today.getTimeInMillis();

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

            String startDayOfWeek = getWeekdayName(event_start);
            String endDayOfWeek = getWeekdayName(event_end);


            boolean fallsOnSelectedDay = (selectedWeekdays.get(startDayOfWeek) || selectedWeekdays.get(endDayOfWeek));

            if((event_start > start_week && event_end < end_week)){

                if(fallsOnSelectedDay){
                    weeklyEvents.put(event_start, event_end);
                }
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

    /**
     * Returns a list of date proposals.
     * Note: These date will be prioritized by doing 1 on each selected day at 9 AM, then 1 on each day at 10 AM, etc.
     * If there is a conflict at 9 am for one selected weekday, the algorithm will move on to the next day.
     * @param conflictingWeeklyEvents is a HashMap of startTimes and endTimes for conflicting events. If events are in this HashMap, do not schedule on top of them
     * @param selectedWeekdays is a HashMap of "Week days" and Booleans for the entire 7 day week.
     */
    private HashMap getCoffeeProposals(HashMap conflictingWeeklyEvents, HashMap selectedWeekdays){

        // initialize return HashMap
        HashMap<Long, Long> proposals = new HashMap<Long, Long>();

        // initialize a week's span in which to suggest dates.
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Calendar agendaRover = new GregorianCalendar();
        agendaRover.set(Calendar.HOUR, 0);
        agendaRover.set(Calendar.MINUTE, 0);
        agendaRover.set(Calendar.SECOND, 0);
        agendaRover.set(Calendar.MILLISECOND, 0); // likely redundant


        int hourOfDay = 9; // start at 9, then loop up until 5 PM
        int optionsProvided = 0; // cut it at 5.
        // Now, using the selected weekdays method, loop through the days needed to set the proper today function.
        // We are gonna brute force it a bit, so we'll need to do a loop

        // double loop that will run a maximum of 105 times.
        // i highly doubt it will ever run that long.
        while(hourOfDay<24 && optionsProvided < 6){
            Iterator weekdayIterator = selectedWeekdays.entrySet().iterator();
            agendaRover.set(Calendar.HOUR_OF_DAY, hourOfDay);
            while(weekdayIterator.hasNext() && optionsProvided < 5){
                Map.Entry mapEntry = (Map.Entry) weekdayIterator.next();


                // this date was a selected preference!
                if((Boolean) mapEntry.getValue()){
                    // mapEntry will tell us what day of the week it is, but we need to set our calendar object to that day!
                    if(!getWeekdayName(agendaRover.getTimeInMillis()).equals(mapEntry.getKey())){
                        // the current weekday we are searching for is ahead of our timestamp (e.g. our timestamp is on Monday, but our weekday search query is Tuesday)
                        while(!getWeekdayName(agendaRover.getTimeInMillis()).equals(mapEntry.getKey())){
                            agendaRover.add(Calendar.DAY_OF_WEEK, 1);
                        }


                    }


                    Long endTime = agendaRover.getTimeInMillis() + TimeUnit.HOURS.toMillis(1); // add an hour to the start time
                    if(!doesConflict(conflictingWeeklyEvents,agendaRover.getTimeInMillis(), endTime)){
                        // make a proposal!
                        proposals.put(agendaRover.getTimeInMillis(), endTime);
                        optionsProvided++;
                    }
                }
            }
            // set back to midnight
            agendaRover.set(Calendar.HOUR_OF_DAY, 0);
            hourOfDay++;
        }
        return proposals;
    }

    /**
     * @param conflictingEvents: hashmap of events that could conflict with the proposed time
     * @param proposedStartTime: Timestamp of proposed event startTime
     * @param proposedEndTime: Timestamp 1 hour after startTime
     * @return a boolean that says whether or not there is a conflict.
     */
    public Boolean doesConflict(HashMap conflictingEvents, Long proposedStartTime, Long proposedEndTime){
        // initialize return value
        boolean answer = false;

        Iterator conflictingIterator = conflictingEvents.entrySet().iterator();
        while (conflictingIterator.hasNext() && !answer){
            Map.Entry mapEntry = (Map.Entry) conflictingIterator.next();
            Long conflictStartTime = (Long) mapEntry.getKey();
            Long conflictEndTime = (Long) mapEntry.getValue();
            String conflictDay = getWeekdayName(conflictStartTime);

            // order: first condition checks that the conflict is later
            //        second condition checks that the conflict is earlier
            answer = !(conflictStartTime > proposedStartTime && conflictEndTime > proposedStartTime || conflictStartTime < proposedStartTime && conflictEndTime < proposedStartTime || conflictEndTime > proposedEndTime && conflictStartTime > proposedEndTime || conflictEndTime < proposedEndTime);
        }

        return answer;
    }

    /**
     * Used to find the day of the week based on a time stamp. Returns something like "Monday" (Definitely capitalized)
     * @param startTime is a timestamp in Long format
     * @return the day of the week in String Format to comply with our HashMap
     */
    private String getWeekdayName(Long startTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

        return simpleDateFormat.format(startTime);
    }

    /**
     *
     * @param map a list of proposed events
     * @return an array of coffee objects that can be called to do various things (like get a time string!)
     */
    public CoffeeEvent[] getCoffeeEvents(HashMap map){
        CoffeeEvent[] answer = new CoffeeEvent[map.size()];
        Iterator iterator = map.entrySet().iterator();

        int i = 0;
        while(iterator.hasNext()){
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            CoffeeEvent cur = new CoffeeEvent((Long) mapEntry.getKey(), (Long) mapEntry.getValue());
            answer[i] = cur;
            i++;
        }

        return answer;
    }


}
