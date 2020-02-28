package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import ca.antonious.materialdaypicker.MaterialDayPicker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;


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


        //configureSendInviteButton();
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
                }

            }
        });

    }
}
