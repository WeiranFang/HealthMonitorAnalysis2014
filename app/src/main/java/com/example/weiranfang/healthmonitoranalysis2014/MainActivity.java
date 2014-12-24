package com.example.weiranfang.healthmonitoranalysis2014;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class MainActivity extends Activity {

    UITableView tableView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tableView = (UITableView) findViewById(R.id.tableView);
        createList();
        Log.d("MainActivity", "total items: " + tableView.getCount());
        tableView.commit();
    }

    private void createList() {
        CustomClickListener listener = new CustomClickListener();
        tableView.setClickListener(listener);
        tableView.addBasicItem("Tweet Heat");
        tableView.addBasicItem("Exercise Duration");
        tableView.addBasicItem("Health Food");
        tableView.addBasicItem("Demography Analysis");
        tableView.addBasicItem("Leader Board");
        tableView.addBasicItem("Heat Map");
        tableView.addBasicItem("Clustering Map");
        tableView.addBasicItem("Picture Map");

        //tableView.addBasicItem("Example 7 - UIButton", "some floating buttons");
        //tableView.addBasicItem("Example 8 - Clear List", "this button will clear the list");

    }

    private class CustomClickListener implements ClickListener {

        @Override
        public void onClick(int index) {
            Log.d("MainActivity", "item clicked: " + index);
            if(index == 0) {
                Intent i = new Intent(MainActivity.this, TweetHeatActivity.class);
                startActivity(i);
            }
            else if(index == 1) {
                Intent i = new Intent(MainActivity.this, ExerciseDurationActivity.class);
                startActivity(i);
            }
            else if(index == 2) {
                Intent i = new Intent(MainActivity.this, HealthFoodViewPagerChartsActivity.class);
                startActivity(i);
            }
            else if (index == 3) {
                Intent i = new Intent(MainActivity.this, DemographyViewPagerChartsActivity.class);
                startActivity(i);
            }
            else if (index == 4) {
                Intent i = new Intent(MainActivity.this, LeaderBoardOverallActivity.class);
                startActivity(i);
            }
            else if (index == 5) {
                Intent i = new Intent(MainActivity.this, HeatMapActivity.class);
                startActivity(i);
            }
            else if (index == 6) {
                Intent i = new Intent(MainActivity.this, ClusteringMapActivity.class);
                startActivity(i);
            }
            else if (index == 7) {
                Intent i = new Intent(MainActivity.this, CustomMarkerClusteringDemoActivity.class);
                startActivity(i);
            }

            /*
            else if(index == 5) {
                Intent i = new Intent(MainActivity.this, Example6Activity.class);
                startActivity(i);
            }
            else if(index == 6) {
                Intent i = new Intent(MainActivity.this, Example7Activity.class);
                startActivity(i);
            }
            else if(index == 7) {
                tableView.clear();
            }
            */

        }

    }

}