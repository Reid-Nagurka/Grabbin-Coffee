package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for permissions
        //https://developer.android.com/training/permissions/requesting#java

        // there might be better ways to do this, but i'm going to create methods for each button
        configureNewEventButton();
        configureSeeHistoryButton();
        configureSettingsButton();
    }

    private void configureNewEventButton(){
        Button newEventButton = findViewById(R.id.newEventButton);
        newEventButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewEvent.class));
            }
        });
    }

    private void configureSeeHistoryButton(){
        Button seeHistoryButton = findViewById(R.id.seeHistoryButton);
        seeHistoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }

    private void configureSettingsButton(){
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
}
