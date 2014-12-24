package com.example.weiranfang.healthmonitoranalysis2014;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;


public class LeaderBoardOverallActivity extends Activity {

    private ListView LeaderBoardOverallListView;
    private JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_overall);

        this.LeaderBoardOverallListView = (ListView) this.findViewById(R.id.lb_overall_list_view);

        new LeaderBoardTask().execute(new JSONSender());

        /*
        this.LeaderBoardOverallListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    // Get the customer which was clicked
                    JSONObject customerClicked = jsonArray.getJSONObject(position);

                    // Send Customer ID
                    Intent showDetails = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
                    showDetails.putExtra("CustomerID", customerClicked.getInt("id"));
                    startActivity(showDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        */

    }


    /*
    public void setTextToTextView(JSONArray jsonArray)
    {
        String s  = "";
        for(int i=0; i<jsonArray.length();i++){

            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                s = s +
                        "Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+
                        "Age : "+json.getInt("Age")+"\n"+
                        "Mobile Using : "+json.getString("Mobile")+"\n\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        this.responseTextView.setText(s);

    }
    */


    public void setListAdapter(JSONArray jsonArray) {

        this.jsonArray = jsonArray;
        this.LeaderBoardOverallListView.setAdapter(new LeaderBoardOverallAdapter(jsonArray, this));

    }

    private class LeaderBoardTask extends AsyncTask<JSONSender,Long,JSONArray> {
        @Override
        protected JSONArray doInBackground(JSONSender... params) {

            // it is executed on Background thread

            return params[0].GetArrayByID(5);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setListAdapter(jsonArray);

        }
    }



}
