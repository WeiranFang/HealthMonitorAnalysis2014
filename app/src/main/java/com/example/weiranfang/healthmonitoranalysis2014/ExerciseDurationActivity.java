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

public class ExerciseDurationActivity extends Activity {

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
        tableView.addBasicItem("Exercise Time in Types", "Exercise duration time of different types");
        tableView.addBasicItem("Exercise Time in States", "Exercise duration time in different states");

    }

    private class CustomClickListener implements ClickListener {

        @Override
        public void onClick(int index) {

            if(index == 0) {

                Intent showETimeType = new Intent(getApplicationContext(), ETimeTypeViewPagerChartsActivity.class);
                startActivity(showETimeType);

            } else if(index == 1) {

                Intent showETimeState = new Intent(getApplicationContext(), ETimeStateViewPagerChartsActivity.class);
                startActivity(showETimeState);

            }
        }

    }

}
