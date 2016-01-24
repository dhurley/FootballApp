package net.djhurley.footballapp;

import static net.djhurley.footballapp.common.Constants.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.djhurley.footballapp.listeners.FootballAppOnItemClickListener;
import net.djhurley.footballapp.tasks.FootballDataRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements FootballAppActivity{

    private FootballDataRequestTask footballDataRequestTask;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.season_list);
        footballDataRequestTask = new FootballDataRequestTask(this);
        footballDataRequestTask.execute(SEASONS_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        footballDataRequestTask.cancel(true);
    }

    @Override
    public void updateUI(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            List<String> seasonNames = getSeasonNames(jsonArray);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_text, android.R.id.text1, seasonNames);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new FootballAppOnItemClickListener(this, jsonArray, SeasonActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
