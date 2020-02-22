package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import ca.antonious.materialdaypicker.MaterialDayPicker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        configureSendInviteButton();
    }

    private void configureSendInviteButton(){
        Button sendInviteButton = findViewById(R.id.sendInviteButton);

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
            }
        });

    }
}
