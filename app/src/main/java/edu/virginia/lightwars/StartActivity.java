package edu.virginia.lightwars;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class StartActivity extends Activity implements LocationListener {

    Preferences pref;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final String TAG = "StartActivity";

    private TextView lat_text;
    private TextView lng_text;
    private TextView alt_text;
    private LocationManager locationManager;
    private String provider;

    private static Twitter twitter;
    private static RequestToken requestToken;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pref = new Preferences(PreferenceManager.getDefaultSharedPreferences(this));

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

        TextView total_text = (TextView) findViewById(R.id.total_text);
        String total_frame = getResources().getString(R.string.total_frame);
        String total_msg = String.format( total_frame, pref.getTotalCorrect() );
        total_text.setText( total_msg );

        final Button conn_button = (Button) findViewById(R.id.conn_button);
        conn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                View view = StartActivity.this.getLayoutInflater().inflate( R.layout.dialog_conn, null );
                AlertDialog.Builder builder = new AlertDialog.Builder( StartActivity.this );

                builder.setTitle(R.string.dialog_title);
                builder.setMessage(R.string.dialog_message);
                builder.setView(view);

                final EditText ip_edit_text = (EditText) view.findViewById(R.id.ip_edit_text);
                ip_edit_text.setText( pref.getIP() );

                builder.setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(StartActivity.this, BeaconActivity.class);
                        String ip = ip_edit_text.getText().toString();
                        intent.putExtra(BeaconActivity.EXTRA_IP, ip);
                        pref.setIP(ip);
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
        final Button login_button = (Button) findViewById(R.id.login_button);
        final Button logout_button = (Button) findViewById(R.id.logout_button);

        tweet_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String oauthAccessToken = pref.getToken();
                String oauthAccessTokenSecret = pref.getSecret();
                Log.d("SA", oauthAccessToken);
                Log.d("SA", oauthAccessTokenSecret);

                String frame = getResources().getString(R.string.tweet_frame);
                String status = String.format( frame, pref.getTotalCorrect() );
                new updateTwitterStatus().execute( status );

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if( !pref.hasCred() ) {
//                    getCreds();
                    new loginToTwitter().execute();
                }

                if( pref.hasCred() ) {
                    tweet_button.setVisibility(View.VISIBLE);
                    logout_button.setVisibility(View.VISIBLE);
                    login_button.setVisibility(View.GONE);
                } else {
                    tweet_button.setVisibility(View.GONE);
                    logout_button.setVisibility(View.GONE);
                    login_button.setVisibility(View.VISIBLE);
                }
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pref.clearCred();

                if( pref.hasCred() ) {
                    tweet_button.setVisibility(View.VISIBLE);
                    logout_button.setVisibility(View.VISIBLE);
                    login_button.setVisibility(View.INVISIBLE);
                } else {
                    tweet_button.setVisibility(View.GONE);
                    logout_button.setVisibility(View.GONE);
                    login_button.setVisibility(View.VISIBLE);
                }
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

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(Constants.CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(Constants.IEXTRA_OAUTH_VERIFIER);
            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                pref.setToken(accessToken.getToken());
                pref.setSecret(accessToken.getTokenSecret());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if( pref.hasCred() ) {
            tweet_button.setVisibility(View.VISIBLE);
            logout_button.setVisibility(View.VISIBLE);
            login_button.setVisibility(View.GONE);
        } else {
            tweet_button.setVisibility(View.GONE);
            logout_button.setVisibility(View.GONE);
            login_button.setVisibility(View.VISIBLE);
        }

    }

    public void getCreds() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
        builder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    requestToken = twitter.getOAuthRequestToken(Constants.CALLBACK_URL);
                    StartActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));

                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    class loginToTwitter extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StartActivity.this);
            pDialog.setMessage("Requesting Twitter Authentication...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(Constants.CALLBACK_URL);
                StartActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
                return true;
            }
            catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(success) {
                        String msg = "Please authorize LightWars";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    class updateTwitterStatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StartActivity.this);
            pDialog.setMessage("Connecting to Twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            Log.d("Tweet Text", "> " + args[0]);
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
                builder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);

                // Access Token
                String access_token = pref.getToken();
                // Access Token Secret
                String access_token_secret = pref.getSecret();

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                // Update status
                twitter4j.Status response = twitter.updateStatus(status);

                Log.d("Status", "> " + response.getText());
            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Successfully Tweeted", Toast.LENGTH_LONG)
                            .show();
                }
            });
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
