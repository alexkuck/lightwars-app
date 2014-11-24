package edu.virginia.lightwars;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class BeaconActivity extends Activity {

    public final static String EXTRA_IP = "com.example.myfirstapp.IP";

    private String ip;

    @Override
    public void onPause()
    {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ip = getIntent().getStringExtra(EXTRA_IP);

        Button red_button = (Button) findViewById(R.id.red_button);
        red_button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        red_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getRedJSON());
            }
        });

        Button green_button = (Button) findViewById(R.id.green_button);
        green_button.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        green_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getGreenJSON());
            }
        });

        Button blue_button = (Button) findViewById(R.id.blue_button);
        blue_button.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        blue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBlueJSON());
            }
        });

        Button white_button = (Button) findViewById(R.id.white_button);
        white_button.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        white_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getWhiteJSON());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

}
