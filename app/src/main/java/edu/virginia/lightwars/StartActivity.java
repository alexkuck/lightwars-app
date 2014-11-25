package edu.virginia.lightwars;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class StartActivity extends Activity implements LocationListener {

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final String TAG = "StartActivity";

    private TextView lat_text;
    private TextView lng_text;
    private TextView alt_text;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        lat_text = (TextView) findViewById(R.id.lat_text);
        lng_text = (TextView) findViewById(R.id.lng_text);
        alt_text = (TextView) findViewById(R.id.alt_text);

        final Button conn_button = (Button) findViewById(R.id.conn_button);
        conn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                View view = StartActivity.this.getLayoutInflater().inflate( R.layout.dialog_conn, null );
                AlertDialog.Builder builder = new AlertDialog.Builder( StartActivity.this );

                builder.setTitle(R.string.dialog_title);
                builder.setMessage(R.string.dialog_message);
                builder.setView(view);

                final EditText ip_edit_text = (EditText) view.findViewById(R.id.ip_edit_text);

                builder.setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(StartActivity.this, BeaconActivity.class);
                        intent.putExtra(BeaconActivity.EXTRA_IP, ip_edit_text.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                });

                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

                final AlertDialog dialog = builder.create();

                ip_edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus) {
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });

                dialog.show();

            }
        });

        final Button tweet_button = (Button) findViewById(R.id.tweet_button);
        tweet_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            Log.d(TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            lat_text.setText("Location not available");
            lng_text.setText("Location not available");
            alt_text.setText("Location not available");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 2000, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double alt = location.getAltitude();

        lat_text.setText(String.valueOf(lat) + " degrees");
        lng_text.setText(String.valueOf(lng) + " degrees");
        alt_text.setText(String.valueOf(alt) + " meters");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "New Provider Enabled: " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Provider Disabled: " + provider, Toast.LENGTH_SHORT).show();
    }
}
