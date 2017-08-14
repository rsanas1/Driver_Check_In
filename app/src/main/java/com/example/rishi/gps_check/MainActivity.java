package com.example.rishi.gps_check;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnShowLocation;
    private static final  int REQUEST_CODE_PERMISSION =2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    TextView location;
    Spinner busSelection;
    String BUS_NO;
    ToggleButton switchLocation;
    boolean permissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = (TextView) findViewById(R.id.locationText);
        busSelection= (Spinner) findViewById(R.id.selectBus);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.buses,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busSelection.setAdapter(adapter);
        busSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BUS_NO= parent.getItemAtPosition(position).toString();
                location.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                location.setVisibility(View.GONE);
            }
        });

        try{
            if (ActivityCompat.checkSelfPermission(this,mPermission)!= MockPackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
                Log.d("MainActivity", "if checkSelfPermission" );

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        btnShowLocation = (Button) findViewById(R.id.button);
        /*btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location.setText(null);
                gps = new GPSTracker(MainActivity.this);
                Log.d("MainActivity", "after gps generation "+gps.getLatitude()+" "+gps.getLongitude());
                if (gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Log.d("MainActivity", "after if can get location "+gps.getLatitude()+" "+gps.getLongitude());
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference(BUS_NO);
                    setFirebaseValue(latitude,longitude);
                }else{
                    Log.d("MainActivity", "show setting ");
                    gps.showSettingsAlert();
                }
            }
        });*/

        switchLocation = (ToggleButton) findViewById(R.id.tgButton);
        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchLocation.setBackgroundColor(Color.GREEN);
                    location.setText(null);
                    gps = new GPSTracker(MainActivity.this);
                    Log.d("MainActivity", "after gps generation "+gps.getLatitude()+" "+gps.getLongitude());
                    if (gps.canGetLocation()){
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        Log.d("MainActivity", "after if can get location "+gps.getLatitude()+" "+gps.getLongitude());
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference(BUS_NO);
                        setFirebaseValue(latitude,longitude);
                    }else{
                        Log.d("MainActivity", "show setting ");
                        gps.showSettingsAlert();
                    }
                }
                else
                {
                    switchLocation.setBackgroundColor(Color.RED);
                    if(gps!=null)
                    {
                        gps.stopUsingGPS();
                        location.setText("Click the Button");
                    }
                }
            }
        });
    }

    public void setFirebaseValue(Double latitude, Double longitude){
        myRef.setValue(latitude+","+longitude);
        location.setText(latitude+""+longitude);
    }
}