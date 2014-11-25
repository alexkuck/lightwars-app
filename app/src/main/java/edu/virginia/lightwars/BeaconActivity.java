package edu.virginia.lightwars;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import twitter4j.Twitter;


public class BeaconActivity extends Activity {

    public final static String EXTRA_IP = "edu.virginia.lightwars.IP";

    private String ip;
    private int index;
    private Integer total;

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
        newMathProblem();
        ip = getIntent().getStringExtra(EXTRA_IP);

        final Button button0 = (Button) findViewById(R.id.button0);
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.button3);

        button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 0) {
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getWhiteJSON( getAndAdd() ));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    button0.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 1) {
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getWhiteJSON( getAndAdd() ));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    button1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 2) {
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getWhiteJSON( getAndAdd() ));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    button2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 3) {
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getWhiteJSON( getAndAdd() ));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    button3.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.beacon, menu);
        return true;
    }

    private static Random random = new Random();
    private void newMathProblem() {
        int mx1 = 75;
        int mx2 = 150;
        int min = 0;

        int q1 = random.nextInt((mx1 - min) + 1) + min;
        int q2 = random.nextInt((mx1 - min) + 1) + min;

        ArrayList<Integer> ans = new ArrayList<Integer>();
        for(int i = 0; i < 3; i++) {
            ans.add(random.nextInt((mx2 - min) + 1) + min);
        }
        int re = q1 + q2;
        ans.add(re);
        Collections.shuffle(ans);
        index = ans.indexOf(re);

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        button0.setText( String.valueOf( ans.get(0)) );
        button1.setText( String.valueOf( ans.get(1)) );
        button2.setText( String.valueOf( ans.get(2)) );
        button3.setText( String.valueOf( ans.get(3)) );

        TextView quest_view = (TextView) findViewById(R.id.question);
        String quest_str = String.valueOf(q1) + " + " + String.valueOf(q2) + " = ?";
        quest_view.setText(quest_str);
    }

    private int getAndAdd() {
        if (total == null || total == 0) {
            total = 32;
        }
        return total--;
    }

}
