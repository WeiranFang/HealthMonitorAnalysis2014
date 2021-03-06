package com.example.weiranfang.healthmonitoranalysis2014;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyItemReader {

    /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";
/*

    public List<MyItem> read(InputStream inputStream) throws JSONException {
        List<MyItem> items = new ArrayList<MyItem>();
        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);

            String name = object.getString("screen_name");
            String type = object.getString("exercise_type");
            String time = object.getString("exercise_time");
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");

            items.add(new MyItem(lat, lng, name, type, time));
        }
        return items;
    }
*/

    public List<MyItem> read(JSONArray jsonArray) throws JSONException {
        List<MyItem> items = new ArrayList<MyItem>();
//        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
//        JSONArray array = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            String name = object.getString("screen_name");
            String type = object.getString("exercise_type");
            String time = object.getString("exercise_time");
            double lat = object.getDouble("geo_lat");
            double lng = object.getDouble("geo_long");

            items.add(new MyItem(lat, lng, name, type, time));
        }
        return items;
    }
}
