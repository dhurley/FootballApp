package net.djhurley.footballapp.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.djhurley.footballapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by djhurley on 24/01/16.
 */
public class MatchdaySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private Activity activity;
    private ListView listView;
    private Map<Integer, List<String>> matchdayFixtureMap;

    public MatchdaySpinnerOnItemSelectedListener(Activity activity, ListView listView, Map<Integer, List<String>> matchdayFixtureMap) {
        super();
        this.activity = activity;
        this.listView = listView;
        this.matchdayFixtureMap = matchdayFixtureMap;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.list_text, android.R.id.text1, matchdayFixtureMap.get(position + 1));
        listView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
