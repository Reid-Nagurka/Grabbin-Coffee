package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
 *
 * This class is used to record user data in terms of events
 * THEN it uses a Push request to our serverless architecture to schedule the event :)
 */
public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
    }
}
