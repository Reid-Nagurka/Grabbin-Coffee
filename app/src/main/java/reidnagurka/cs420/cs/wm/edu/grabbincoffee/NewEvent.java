package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import ca.antonious.materialdaypicker.MaterialDayPicker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/*
 *
 * This class is used to record user data in terms of events
 * THEN it uses a Push request to our serverless architecture to schedule the event :)
 */
public class NewEvent extends AppCompatActivity {

    //private boolean loc_found_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);


        final EditText eventNameField = findViewById(R.id.eventTitleField);
        eventNameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });

        final EditText eventEmailField = findViewById(R.id.inviteEmailField);
        eventEmailField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });


        final EditText eventLocationField = findViewById(R.id.eventLocationField);
        eventLocationField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                return;
            }
        });


        configureSendInviteButton();
    }

    /* Run everytime data is submitted*/
    private void validateInput() {
        // check that no field is empty
        // check email is valid
        // run configureSendInviteButton()

        final EditText eventNameField = findViewById(R.id.eventTitleField);
        final EditText eventEmailField = findViewById(R.id.inviteEmailField);
        final EditText eventLocationField = findViewById(R.id.eventLocationField);

//        final MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);

        final String eventNameText = eventNameField.getText().toString();
        final String eventEmailText = eventEmailField.getText().toString();
        final String eventLocationText = eventLocationField.getText().toString();
//        final List<MaterialDayPicker.Weekday> chosenDays = materialDayPicker.getSelectedDays(); // perhaps we should change this to be a string..


        //check that no field is empty
        if (!eventNameText.isEmpty() && !eventEmailText.isEmpty() && !eventLocationText.isEmpty()) {

//            if (chosenDays.isEmpty()) {
//                Snackbar snackbar = Snackbar
//                        .make(findViewById(android.R.id.content).getRootView(), "Please fill out all fields!", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            } else {

                //check email is valid
//                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//                boolean check = eventEmailText.matches(regex);
//                if (check)
                    configureSendInviteButton();
//            }
        }

    }

    private void configureSendInviteButton(){
        Button sendInviteButton = findViewById(R.id.sendInviteButton);
        sendInviteButton.setVisibility(View.VISIBLE);

        // editText fields to get field data
        final EditText eventNameField = findViewById(R.id.eventTitleField);
        final EditText eventEmailField = findViewById(R.id.inviteEmailField);
        final EditText eventLocationField = findViewById(R.id.eventLocationField);

//        final MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);



        sendInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something with these things!
                final String eventNameText = eventNameField.getText().toString();
                final String eventEmailText = eventEmailField.getText().toString();
                final String eventLocationText = eventLocationField.getText().toString();
//                final List<MaterialDayPicker.Weekday> chosenDays = materialDayPicker.getSelectedDays(); // perhaps we should change this to be a string..
//                Log.d("Days chosen: ", materialDayPicker.getSelectedDays().toString()); // this works!

                final CheckBox monday = findViewById(R.id.mondayCheckBox);
                final CheckBox tuesday = findViewById(R.id.tuesdayCheckBox);
                final CheckBox wednesday = findViewById(R.id.wednesdayCheckBox);
                final CheckBox thursday = findViewById(R.id.thursdayCheckBox);
                final CheckBox friday = findViewById(R.id.fridayCheckBox);
                final CheckBox saturday = findViewById(R.id.saturdayCheckBox);
                final CheckBox sunday = findViewById(R.id.sundayCheckBox);

                //  check that all fields are filled out
                //  if not, prompt the user to fill everyting out
                // this version is missing ChosenDays, just a psa
                if (eventNameText.isEmpty() || eventEmailText.isEmpty() || eventLocationText.isEmpty()) {
//                    Snackbar snackbar = Snackbar
//                            .make(findViewById(android.R.id.content).getRootView(), "Please fill out all fields!", Snackbar.LENGTH_LONG);
//                    snackbar.show();
                }

                else {

                    final HashMap<String, Boolean> selectedWeekdays = new HashMap<>();
                    selectedWeekdays.put("Monday", monday.isChecked());
                    selectedWeekdays.put("Tuesday", tuesday.isChecked());
                    selectedWeekdays.put("Wednesday", wednesday.isChecked());
                    selectedWeekdays.put("Thursday", thursday.isChecked());
                    selectedWeekdays.put("Friday", friday.isChecked());
                    selectedWeekdays.put("Saturday", saturday.isChecked());
                    selectedWeekdays.put("Sunday", sunday.isChecked());
                    CoffeeEvent[] proposedEvents = runCalendarQuery(selectedWeekdays);

                    // TODO: add a handler for no proposed events found!
                    if(proposedEvents.length > 0){
                        sendPostRequest(proposedEvents);
                    }
                }

            }
        });

    }

    /**
     * Sends the request to the API.
     * @param proposals is an array of sorted CoffeeEvents.
     */
    private void sendPostRequest(final CoffeeEvent[] proposals){
        final EditText eventEmailField = findViewById(R.id.inviteEmailField);
        final EditText eventTitleField = findViewById(R.id.eventTitleField);
        final EditText eventLocationField = findViewById(R.id.eventLocationField);
        final EditText inviteeNameField = findViewById(R.id.inviteeName);
        final Button sendInviteButton = findViewById(R.id.sendInviteButton);
        RequestQueue queue = Volley.newRequestQueue(NewEvent.this);
        String url = "https://grabbin-coffee-api.now.sh/api/invite"; // TODO: ENSURE THIS IS STILL ACCURATE
//        String url = "https://6377acb2.ngrok.io/api/invite";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sendInviteButton.setText("Invitation Sent!");
                sendInviteButton.setEnabled(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendInviteButton.setText("Invitation send error :(");
                sendInviteButton.setEnabled(false);

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", eventEmailField.getText().toString()); //TODO: add sender email!!

                // how many proposals are we sending?
                int size = proposals.length;
                params.put("count", Integer.toString(size));

                String rsvpEmail = eventEmailField.getText().toString();
                // create basic mailTo link object that will be used throughout.
                MailLinkCreator mailLinkCreator = new MailLinkCreator(inviteeNameField.getText().toString(),
                        "App Owner Human Name ((remember to collect this))", rsvpEmail,
                        "greg.garnhart12+ADDTHIS@gmail.com",proposals[0].getStartDateOnly(), proposals[0].getEndTimeOnly());

                // add proposals to params object!
                for(int i = 0; i < size; i++){
                    String dateKey = "date_"+ (i+1);
                    String timeKey = "time_"+ (i+1);
                    String urlKey = "event_link_"+ (i+1);
                    String dateString = proposals[i].getStartDateOnly();
                    String timeString = proposals[i].getStartTimeOnly();

                    mailLinkCreator.setEventDateOnly(dateString);
                    mailLinkCreator.setEventTimeOnly(timeString);
                    String url = mailLinkCreator.getMailLink();

                    params.put(dateKey, dateString);
                    params.put(timeKey, timeString);
                    params.put(urlKey, url);
                }
                params.put("sender_email", "gegarnhart@email.wm.edu");
                params.put("sender_name", "Greg");
                params.put("decline_link",mailLinkCreator.getDeclineLink());
                params.put("subject", "You are being invited to get coffee!");
                params.put("event_title", eventTitleField.getText().toString());
                params.put("name", inviteeNameField.getText().toString());
                params.put("location", eventLocationField.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };
        queue.add(stringRequest);
    }

    /* Open up webview to Google Maps, let user pick a location, copy location and paste into text field*/
    public void locationHandler(View view) {
        //need to check for location permission
        //startActivity(new Intent(NewEvent.this, MapsActivity.class));

        if (ContextCompat.checkSelfPermission(NewEvent.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Fine location permission Not Granted");

            //need to get Permissions
            //100 is a temporary request code, should be a CONST
            ActivityCompat.requestPermissions(NewEvent.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);
        }
        //permission is granted
        else {
           // loc_found_check = false;
            //make a location request so that we get the location and can pass it to launch google maps app
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locRes) {
                    if (locRes == null) {
                        //Log.d("Loc", "Location is null in if");
                        System.out.println("Location is null in if");
                        return;
                    } else {
                        //Log.d("Loc", "Location is not null in else");
                        System.out.println("Location is not null in else");
                        //loc_found_check = true;
                    }
                }
            };

            //if (loc_found_check == true) {
                FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                System.out.println(locationResult);
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                //  Log.d("Location: ", location.toString());
                                if (location != null) {
                                    String latitude = Double.toString(location.getLatitude());
                                    String longititude = Double.toString(location.getLongitude());

                                    String uri_parse_string = "geo:" + latitude + "," + longititude + "?q=coffee";
                                    Log.d("uri", uri_parse_string);
                                    Uri gmmIntentUri = Uri.parse(uri_parse_string);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(mapIntent);
                                    }

                                }
                            }
                        });
       //   }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        //more cases for more permissions, for right now 100 is just a placeholder, should be switched to CONST
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("Permission was granted, going to new screen from override");
                                        //startActivity(new Intent(NewEvent.this, MapsActivity.class));
                    FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    Task locationResult = mFusedLocationProviderClient.getLastLocation();
                    System.out.println(locationResult);
                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationCallback locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locRes) {
                            if (locRes == null) {
                                //Log.d("Loc", "Location is null in if");
                                System.out.println("Location is null in if");
                                return;
                            } else {
                                //Log.d("Loc", "Location is not null in else");
                                System.out.println("Location is not null in else");
                                //loc_found_check = true;
                            }
                        }
                    };
                    mFusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.

                                    if (location != null) {
                                        Log.d("Location: ", location.toString());
                                        String latitude = Double.toString(location.getLatitude());
                                        String longititude = Double.toString(location.getLongitude());

                                        String uri_parse_string = "geo:" + latitude + "," + longititude + "?q=coffee";
                                        Log.d("uri", uri_parse_string);
                                        Uri gmmIntentUri = Uri.parse(uri_parse_string);
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(mapIntent);
                                        }

                                    }
                                }
                            });

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("User denied permission");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    // GREG's Additions

    /**
     * searches for good meet times
     * @param selectedWeekdays days the user wants to meet
     * @return an array of up to 5 coffeeEvent objects (possible times the meeting could take place)
     */
    private CoffeeEvent[] runCalendarQuery(HashMap<String,Boolean> selectedWeekdays){

        if (ContextCompat.checkSelfPermission(NewEvent.this, Manifest.permission.READ_CALENDAR)
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



            // NOTE: this was copy and pasted. i don't think we need them (AND it was causing errors!)
//            calID = cur.getLong(PROJECTION_ID_INDEX);
//            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
//            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

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
    public boolean doesConflict(HashMap conflictingEvents, Long proposedStartTime, Long proposedEndTime){
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
