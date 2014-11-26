package edu.virginia.lightwars;

import android.content.SharedPreferences;

/**
 * Created by askuck on 11/25/14.
 */
public class Preferences {

    public static final String PREF_KEY_IP = "ip";
    public static final String PREF_KEY_TOKEN = "token";
    public static final String PREF_KEY_SECRET = "secret";
    public static final String PREF_KEY_TOTAL = "correct";


    private final SharedPreferences prefs;
    public Preferences(final SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public boolean hasCred() {
        String token = prefs.getString(PREF_KEY_TOKEN, null);
        String secret = prefs.getString(PREF_KEY_SECRET, null);
        return token != null || secret != null;
    }

    public void clearCred() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PREF_KEY_TOKEN);
        editor.remove(PREF_KEY_SECRET);
        editor.commit();
    }


    public void setToken(String x) {
        prefs.edit().putString(PREF_KEY_TOKEN, x).commit();
    }

    public String getToken() {
        return prefs.getString(PREF_KEY_TOKEN, "");
    }

    public void setSecret(String x) {
        prefs.edit().putString(PREF_KEY_SECRET, x).commit();
    }

    public String getSecret() {
        return prefs.getString(PREF_KEY_SECRET, "");
    }


    public String getIP() {
        return prefs.getString(PREF_KEY_IP, "");
    }

    public void setIP(String ip) {
        prefs.edit().putString(PREF_KEY_IP, ip).commit();
    }


    public void setTotalCorrect(int x) {
        prefs.edit().putInt(PREF_KEY_TOTAL, x).commit();
    }

    public int getTotalCorrect() {
        return prefs.getInt(PREF_KEY_TOTAL, 0);
    }


}
