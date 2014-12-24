package com.example.weiranfang.healthmonitoranalysis2014;

/**
 * Created by weiranfang on 11/24/14.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class TweetHeatActivity extends Activity {

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
        //tableView.addBasicItem("Users Leader Board", "Leader board for Tweeter users");
        tableView.addBasicItem("Area Exercise Intensity", "Exercise intensity in all states.");
        tableView.addBasicItem("Exercise Types Intensity", "Exercise intensity tweets of all types.");
        tableView.addBasicItem("Exercise Tendency of Day", "Exercise intensity of time periods varied by type");
        //tableView.addBasicItem("Demography Analysis", "Exercise intensity in different age and gender");
    }

    private class CustomClickListener implements ClickListener {

        @Override
        public void onClick(int index) {
            if(index == 0) {
                //ei_area_overall

                Intent showEiAreaOverall = new Intent(getApplicationContext(), EiAreaOverallPreviewColumnChartActivity.class);
                //showEiAreaOverall.putExtra("CustomerID", customerClicked.getInt("id"));
                startActivity(showEiAreaOverall);

            } else if(index == 1) {
                //ei_type_overall

                Intent showEiTypeOverall = new Intent(getApplicationContext(), EiTypeOverallPieChartActivity.class);
                startActivity(showEiTypeOverall);

            } else if(index == 2) {
                //ei_time_type

                Intent showEiTimeType = new Intent(getApplicationContext(), EiTimeTypeLineColumnDependencyActivity.class);
                startActivity(showEiTimeType);

            }

        }

    }

}
