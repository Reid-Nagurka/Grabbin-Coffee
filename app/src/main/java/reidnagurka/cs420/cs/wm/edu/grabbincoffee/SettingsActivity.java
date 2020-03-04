package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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

        emailAddressHandler();
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




        postUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // A HashMap of our selected weekdays.
                // Important to note that the capitilization is important!
                // It is compared to a string returned from a calendar function

            }
        });
    }

    private void emailAddressHandler() {
        final EditText email_input = findViewById(R.id.userEmailField);

        email_input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //runs once user has clicked submit key on keyboard
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    //gets byte array from email field
                    byte[] bytes = email_input.getText().toString().getBytes();
//                    for(int i = 0; i < bytes.length; i++) {
//                        System.out.println("byte: " + bytes[i]);
//                    }
//                    String s = new String(bytes);
//                    System.out.println(s);

                    //filename used for storage
                    String filename = "coffee_app_storage.txt";

                    //gets file in the private app storage w filename above and writes to it
                    try (FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE)) {
                        fos.write(bytes);
                        
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.out.println("File not found fos");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //reading back, will go elsewhere but proof of working code here
                    try {
                        FileInputStream fis = openFileInput(filename);
                        //checks to make sure there is data to be read
                        if (fis.available() > 0) {
                            //reads in bytes from file to a byte array
                            byte[] read_bytes = new byte[fis.available()];
                            fis.read(read_bytes);
                            //converts back byte array to string
                            String email_as_string = new String(bytes);
                            System.out.println("Reading back final: " + email_as_string);
                        }
                    }
                    catch (FileNotFoundException e) {
                        System.out.println("File not found fis");
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });

    }


}
