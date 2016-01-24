package net.djhurley.footballapp.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import net.djhurley.footballapp.FootballAppActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static net.djhurley.footballapp.common.Constants.FOOTBALL_API_TOKEN_KEY;
import static net.djhurley.footballapp.common.Constants.GET_REQUEST;
import static net.djhurley.footballapp.common.Constants.X_AUTH_TOKEN;

/**
 * Created by djhurley on 23/01/16.
 */
public class FootballDataRequestTask extends AsyncTask<String, String, String> {
    private FootballAppActivity activity;

    public FootballDataRequestTask(FootballAppActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... args) {
        String urlAddress = args[0];
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(GET_REQUEST);
            urlConnection.setRequestProperty(X_AUTH_TOKEN, FOOTBALL_API_TOKEN_KEY);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return result.toString();
    }

    @Override
    protected void onPostExecute(String json) {
        activity.updateUI(json);
    }
}
