package net.djhurley.footballapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import net.djhurley.footballapp.listeners.MatchdaySpinnerOnItemSelectedListener;
import net.djhurley.footballapp.tasks.FootballDataRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.djhurley.footballapp.common.Constants.*;

public class SeasonActivity extends AppCompatActivity implements FootballAppActivity{

    private FootballDataRequestTask footballDataRequestTask;
    private Spinner matchdaySpinner;
    private ListView listView;
    private HashMap<Integer, List<String>> matchdayFixtureMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        matchdaySpinner = (Spinner) findViewById(R.id.matchday_spinner);
        listView = (ListView) findViewById(R.id.fixture_list);

        Intent intent = getIntent();
        String json = intent.getStringExtra(SEASON_MESSAGE);

        try {
            String url = getFixturesUrl(json);
            footballDataRequestTask = new FootballDataRequestTask(this);
            footballDataRequestTask.execute(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        footballDataRequestTask.cancel(true);
    }

    @Override
    public void updateUI(String json) {
        Map<Integer, List<String>> fixtures = getFixtures(json);
        List<String> matchdays = getMatchdays(fixtures);
        matchdayFixtureMap = (HashMap<Integer, List<String>>) fixtures;

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, matchdays);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matchdaySpinner.setAdapter(spinnerAdapter);
        matchdaySpinner.setOnItemSelectedListener(new MatchdaySpinnerOnItemSelectedListener(this, listView, matchdayFixtureMap));

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.list_text, android.R.id.text1, matchdayFixtureMap.get(1));
        listView.setAdapter(listAdapter);
    }

    private String getFixturesUrl(String json) throws JSONException {
        return new JSONObject(json).getJSONObject("_links").getJSONObject("fixtures").getString("href");
    }

    private Map<Integer, List<String>> getFixtures(String json){
        Map<Integer, List<String>> fixturesMap = new HashMap<>();
        List<String> fixtureNames = new ArrayList<>();
        int currentMatchday = 1;

        try {
            JSONArray fixtures = new JSONObject(json).getJSONArray("fixtures");
            for (int i = 0; i < fixtures.length(); ++i) {
                JSONObject fixture = fixtures.getJSONObject(i);

                int matchday = Integer.parseInt(fixture.getString("matchday"));

                if(currentMatchday < matchday) {
                    fixturesMap.put(currentMatchday, fixtureNames);
                    currentMatchday = matchday;
                    fixtureNames = new ArrayList<>();
                }

                String homeTeamName = fixture.getString("homeTeamName");
                String goalsHomeTeam = fixture.getJSONObject("result").getString("goalsHomeTeam");
                String awayTeamName = fixture.getString("awayTeamName");
                String goalsAwayTeam = fixture.getJSONObject("result").getString("goalsAwayTeam");

                if(goalsHomeTeam.equals("null") && goalsAwayTeam.equals("null")){
                    fixtureNames.add(homeTeamName + " - " + awayTeamName);
                }else {
                    fixtureNames.add(homeTeamName + " " + goalsHomeTeam + " - " + goalsAwayTeam + " " + awayTeamName);
                }
            }
        }catch (Exception e){
            System.err.println(e.getMessage().toString());
        }

        return fixturesMap;
    }


    private List<String> getMatchdays(Map<Integer, List<String>> fixtures) {
        List<String> matchdays = new ArrayList<>();

        List<Integer> matchdayNumbers = new ArrayList<>(fixtures.keySet());
        Collections.sort(matchdayNumbers);
        for(int i = 0; i < matchdayNumbers.size(); i++){
            matchdays.add("Matchday: " + matchdayNumbers.get(i));
        }

        return matchdays;
    }
}
