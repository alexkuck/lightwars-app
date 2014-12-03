package edu.virginia.lightwars;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class BeaconActivity extends Activity {

    Preferences pref;

    public final static String EXTRA_IP = "edu.virginia.lightwars.IP";

    private String ip;
    private int index;
    private Integer total;

    private Boolean join;
    private Integer green_count;
    private Integer blue_count;


    @Override
    public void onPause()
    {
        pref.setTotalCorrect(total);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = new Preferences(PreferenceManager.getDefaultSharedPreferences(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        newMathProblem();
        ip = getIntent().getStringExtra(EXTRA_IP);
        total = pref.getTotalCorrect();

        PostService.startRPiPOST(this, ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));

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
                    addGreen();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    addBlue();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    button0.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 1) {
                    addGreen();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    addBlue();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    button1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 2) {
                    addGreen();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    addBlue();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    button2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if(index == 3) {
                    addGreen();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
                    newMathProblem();

                    button0.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button1.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                    button3.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                } else {
                    addBlue();
                    PostService.startRPiPOST(getBaseContext(), ip, LEDJSON.getBattleJSON(getBlue(), getGreen()));
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

    private int green_init = 33; //33
    private int blue_init = 1; //1
    private int addGreen() {
        if(green_count == null) {
            green_count = green_init;
        }
        green_count--;
        if (green_count == blue_init) {
            join = false;
            blue_count = blue_init;
            green_count = green_init;

            total++;
            Toast.makeText(this, getResources().getString(R.string.toast_victory), Toast.LENGTH_LONG).show();
            for(int i=0; i<5; i++) {
                PostService.startRPiPOST(this, ip, LEDJSON.getGreenJSON(blue_init));
            }
        }
        if(checkJoin()) {
            blue_count = green_count;
        }
        return green_count;
    }

    private int addBlue() {
        if(blue_count == null) {
            blue_count = blue_init;
        }
        blue_count++;
        if (blue_count == green_init) {
            join = false;
            blue_count = blue_init;
            green_count = green_init;

            for(int i=0; i<5; i++) {
                PostService.startRPiPOST(this, ip, LEDJSON.getRedJSON(blue_init));
            }
        }
        if(checkJoin()) {
            green_count = blue_count;
        }
        return blue_count;
    }

    private int getGreen() {
        if (green_count == null || green_count == blue_init) {
            green_count = green_init;
        }
        return green_count;
    }

    private int getBlue() {
        if (blue_count == null || blue_count == green_init) {
            blue_count = blue_init;
        }
        return blue_count;
    }

    private boolean checkJoin() {
        if (join == null) {
            join = false;
        }
        if(getGreen() - getBlue() == 0) {
            join = true;
        }
        return join;
    }

}
