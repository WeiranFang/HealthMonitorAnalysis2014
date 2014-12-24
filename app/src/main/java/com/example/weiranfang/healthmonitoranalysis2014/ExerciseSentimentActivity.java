package com.example.weiranfang.healthmonitoranalysis2014;

/**
 * Created by weiranfang on 11/24/14.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class ExerciseSentimentActivity extends Activity {

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
        tableView.addBasicItem("Example 1", "Summary text 1");
        tableView.addBasicItem("Example 2", "Summary text 2");
        tableView.addBasicItem("Example 3", "Summary text 3");
        tableView.addBasicItem("Example 4", "Summary text 4");
        tableView.addBasicItem("Example 5", "Summary text 5");
        tableView.addBasicItem("Example 6", "Summary text 6");
        tableView.addBasicItem("Example 7", "Summary text 7");
        tableView.addBasicItem("Example 8", "Summary text 8");
        tableView.addBasicItem("Example 9", "Summary text 9");
        tableView.addBasicItem("Example 10", "Summary text 10");
        tableView.addBasicItem("Example 11");
    }

    private class CustomClickListener implements ClickListener {

        @Override
        public void onClick(int index) {
            Toast.makeText(ExerciseSentimentActivity.this, "item clicked: " + index, Toast.LENGTH_SHORT).show();
        }

    }

}
