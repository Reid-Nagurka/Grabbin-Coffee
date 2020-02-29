package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import ca.antonious.materialdaypicker.MaterialDayPicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *
 * This class is used to record user data in terms of events
 * THEN it uses a Push request to our serverless architecture to schedule the event :)
 */
public class NewEvent extends AppCompatActivity {

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

        final MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);

        final String eventNameText = eventNameField.getText().toString();
        final String eventEmailText = eventEmailField.getText().toString();
        final String eventLocationText = eventLocationField.getText().toString();
        final List<MaterialDayPicker.Weekday> chosenDays = materialDayPicker.getSelectedDays(); // perhaps we should change this to be a string..


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

        final MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);



        sendInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something with these things!
                final String eventNameText = eventNameField.getText().toString();
                final String eventEmailText = eventEmailField.getText().toString();
                final String eventLocationText = eventLocationField.getText().toString();
                final List<MaterialDayPicker.Weekday> chosenDays = materialDayPicker.getSelectedDays(); // perhaps we should change this to be a string..
//                Log.d("Days chosen: ", materialDayPicker.getSelectedDays().toString()); // this works!

                System.out.println("send request");

                //  check that all fields are filled out
                //  if not, prompt the user to fill everyting out
                if (eventNameText.isEmpty() || eventEmailText.isEmpty() || eventLocationText.isEmpty() || chosenDays.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content).getRootView(), "Please fill out all fields!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                else {
                    //  send the request
                    System.out.println("Send Request");
                    sendPostRequest();
                }

            }
        });

    }

    private void sendPostRequest(){
        final EditText eventEmailField = findViewById(R.id.inviteEmailField);
        final Button sendInviteButton = findViewById(R.id.sendInviteButton);
        RequestQueue queue = Volley.newRequestQueue(NewEvent.this);
        String url = "https://grabbin-coffee-api.now.sh/api/invite"; // TODO: ENSURE THIS IS STILL ACCURATE
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", eventEmailField.getText().toString());
                params.put("birthday", "jan 12");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };
        queue.add(stringRequest);
    }

    /* Open up webview to Google Maps, let user pick a location, copy location and paste into text field*/
    public void locationHandler(View view) {
        System.out.println("location handler clicked");
        //check if location permission is granted, request if not

        if (ContextCompat.checkSelfPermission(NewEvent.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission Not Granted");

            //need to get Permissions
            //100 is a temporary request code, should be a CONST
            ActivityCompat.requestPermissions(NewEvent.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);
        }
        //permission is granted
        else {
            System.out.print("Permission was already granted, going to new screen from else statement");
            startActivity(new Intent(NewEvent.this, MapsActivity.class));
        }



        /* In MapsActivity, open up to user's location.*/
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
                    startActivity(new Intent(NewEvent.this, MapsActivity.class));

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
}
