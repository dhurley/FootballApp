package net.djhurley.footballapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
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

import static net.djhurley.footballapp.common.Constants.*;

public class SeasonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        Intent intent = getIntent();
        String json = intent.getStringExtra(SEASON_MESSAGE);

        try {
            JSONObject jsonObject = new JSONObject(json);
            String url = jsonObject.getJSONObject("_links").getJSONObject("fixtures").getString("href");
            System.out.println(url);
            new RequestTask().execute(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class RequestTask extends AsyncTask<String, String, String> {
        private ListView listView;
        private HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {
            return HttpUtilities.getStringResponse(args[0]);
        }

        @Override
        protected void onPostExecute(String json) {
            List<String> fixtures = getFixtures(json);

            listView = (ListView) findViewById(R.id.fixture_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SeasonActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, fixtures);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int itemPosition = position;
                    String itemValue = (String) listView.getItemAtPosition(position);

                    Toast.makeText(getApplicationContext(),
                            "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                            .show();
                }
            });
        }

        private List<String> getFixtures(String json){
            List<String> fixtureNames = new ArrayList<>();

            try {
                JSONArray fixtures = new JSONObject(json).getJSONArray("fixtures");
                for (int i = 0; i < fixtures.length(); ++i) {
                    JSONObject fixture = fixtures.getJSONObject(i);
                    System.out.println(fixture.getString("homeTeamName") + " - " + fixture.getString("awayTeamName"));
                    fixtureNames.add(fixture.getString("homeTeamName") + " - " + fixture.getString("awayTeamName"));
                }
            }catch (Exception e){
                System.err.println(e.getMessage().toString());
            }

            return fixtureNames;
        }

    }
}
