package edu.virginia.lightwars;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PostService extends IntentService {
    private static final String TAG = "PostService";

    private static final String ACTION_POST = "edu.virginia.lightwars.action.POST";
    private static final String EXTRA_IP_ADDRESS = "edu.virginia.lightwars.extra.ip_address";
    private static final String EXTRA_JSON = "edu.virginia.lightwars.extra.json";

    private Handler handler;

    public static void startRPiPOST(Context context, String ip, String json) {
        Intent intent = new Intent(context, PostService.class);
        intent.setAction(ACTION_POST);
        intent.putExtra(EXTRA_IP_ADDRESS, ip);
        intent.putExtra(EXTRA_JSON, json);
        context.startService(intent);
    }

    public PostService() {
        super("PostService");
    }

    @Override
    public void onCreate() {
        handler = new Handler();
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST.equals(action)) {
                final String ip = intent.getStringExtra(EXTRA_IP_ADDRESS);
                final String json = intent.getStringExtra(EXTRA_JSON);
                handleActionFoo(ip, json);
            }
        }
    }


    private void handleActionFoo(String ip, String json) {

        Log.d(TAG, "ip   | " + ip);
        Log.d(TAG, "json | " + json);

        if(ip == null || ip.equalsIgnoreCase(""))
        {
            toast("FAIL\nPlease enter a valid IP address.");
            return;
        }

        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost("http://" + ip + "/rpi");

        //passes the results to a string builder/entity
        StringEntity se = null;
        try
        {
            se = new StringEntity(json);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        //sets the post request as the resulting string
        httpost.setEntity(se);

        try
        {
            httpclient.execute(httpost);
            toast("Executed HTTP POST request.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            toast("Error during HTTP POST request.");
        }

    }


    private void toast(final String msg){
        handler.post(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(PostService.this, msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

}
