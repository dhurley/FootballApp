package net.djhurley.footballapp;

import static net.djhurley.footballapp.common.Constants.*;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.djhurley.footballapp.utils.HttpUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RequestTask().execute(SEASONS_URL);
    }

    private class RequestTask extends AsyncTask<String, String, String> {
        private ListView listView;
        private HttpURLConnection urlConnection;
        private JSONArray jsonArray = null;

        @Override
        protected String doInBackground(String... args) {
            return HttpUtilities.getStringResponse(args[0]);
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                jsonArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<String> seasonNames = getSeasonNames(jsonArray);

            listView = (ListView) findViewById(R.id.season_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, seasonNames);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SeasonActivity.class);
                    try {
                        System.out.println(jsonArray.getJSONObject(position).toString());
                        intent.putExtra(SEASON_MESSAGE, jsonArray.getJSONObject(position).toString());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private List<String> getSeasonNames(JSONArray json){
            List<String> seasonNames = new ArrayList<>();

            try {
                for (int i = 0; i < json.length(); ++i) {
                    JSONObject season = json.getJSONObject(i);
                    seasonNames.add(season.getString(CAPTION_KEY));
                }
            }catch (Exception e){
                System.err.println(e.getMessage().toString());
            }

            return seasonNames;
        }

    }
}
