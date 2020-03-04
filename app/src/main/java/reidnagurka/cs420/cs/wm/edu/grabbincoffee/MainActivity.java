package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    private final int CAL_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for permissions
        //https://developer.android.com/training/permissions/requesting#java

        // there might be better ways to do this, but i'm going to create methods for each button
        configureNewEventButton();
        configureSettingsButton();
    }

    private void configureNewEventButton(){
        Button newEventButton = findViewById(R.id.newEventButton);
        newEventButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                //first check permissions, if permissions are needed, ask user. Then continue
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {
                            System.out.println("Permission Not Granted");

                            //need to get Permissions
                           //100 is a temporary request code, should be a CONST
                            ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_CALENDAR},
                                    CAL_PERMISSION_REQUEST_CODE);
                }
                //permission is granted
                else {
                    System.out.print("Permission was already granted, going to new screen from else statement");
                    startActivity(new Intent(MainActivity.this, NewEvent.class));
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        //more cases for more permissions, for right now 100 is just a placeholder, should be switched to CONST
        switch (requestCode) {
            case CAL_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("Permission was granted, going to new screen from override");
                    startActivity(new Intent(MainActivity.this, NewEvent.class));

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
