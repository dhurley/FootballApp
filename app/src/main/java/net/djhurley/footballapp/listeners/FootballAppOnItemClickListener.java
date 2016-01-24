package net.djhurley.footballapp.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import net.djhurley.footballapp.SeasonActivity;

import org.json.JSONArray;
import org.json.JSONException;

import static net.djhurley.footballapp.common.Constants.SEASON_MESSAGE;

/**
 * Created by djhurley on 23/01/16.
 */
public class FootballAppOnItemClickListener implements AdapterView.OnItemClickListener {

    private Context context;
    private JSONArray jsonArray;
    private Class zlass;

    public FootballAppOnItemClickListener(Context context, JSONArray jsonArray, Class zlass){
        super();
        this.context = context;
        this.jsonArray=jsonArray;
        this.zlass = zlass;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, zlass);
        try {
            System.out.println(jsonArray.getJSONObject(position).toString());
            intent.putExtra(SEASON_MESSAGE, jsonArray.getJSONObject(position).toString());
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
