package com.example.weiranfang.healthmonitoranalysis2014;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.ViewportChangeListener;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;

public class EiAreaOverallPreviewColumnChartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_column_chart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public final static String[] states = new String[] { "AK", "AL", "AR", "AZ", "CA", "CO", "CT",
                "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME",
                "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH",
                "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV",
                "WY"};

        private ColumnChartView chart;
        private PreviewColumnChartView previewChart;
        private ColumnChartData data;
        /**
         * Deep copy of data.
         */
        private ColumnChartData previewData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_preview_column_chart, container, false);

            chart = (ColumnChartView) rootView.findViewById(R.id.chart);

            previewChart = (PreviewColumnChartView) rootView.findViewById(R.id.chart_preview);

            // Generate data for previewed chart and copy of that data for preview chart.
            //generateData();

            new GenerateDataTask().execute(new JSONSender());

            //generateData(new JSONSender().GetArrayByID(0));


            /*
            chart.setColumnChartData(data);

            // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
            // zoom/scroll is unnecessary.
            chart.setZoomEnabled(false);
            chart.setScrollEnabled(false);

            previewChart.setColumnChartData(previewData);
            previewChart.setViewportChangeListener(new ViewportListener());

            previewX(false);
            */

            return rootView;
        }

        // MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.preview_column_chart, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                new GenerateDataTask().execute(new JSONSender());
                chart.setColumnChartData(data);
                previewChart.setColumnChartData(previewData);
                previewX(true);
                return true;
            }
            if (id == R.id.action_preview_both) {
                previewXY();
                previewChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
                return true;
            }
            if (id == R.id.action_preview_horizontal) {
                previewX(true);
                return true;
            }
            if (id == R.id.action_preview_vertical) {
                previewY();
                return true;
            }
            if (id == R.id.action_change_color) {
                int color = Utils.pickColor();
                while (color == previewChart.getPreviewColor()) {
                    color = Utils.pickColor();
                }
                previewChart.setPreviewColor(color);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void generateData(JSONArray jsonArray) {
            //int numSubcolumns = 1;
            int numColumns = 50;
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<ColumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<ColumnValue>();

                try {
                    // get specific data from the json array
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int columnData = jsonObject.getInt("state_count");

                    // add data to the column values
                    values.add(new ColumnValue((float) columnData, Utils.pickColor()));
                    axisValues.add(new AxisValue(i, states[i].toCharArray()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //values.add(new ColumnValue((float) Math.random() * 50f + 5, Utils.pickColor()));

                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            data = new ColumnChartData(columns);
            data.setAxisXBottom(new Axis(axisValues));
            data.setAxisYLeft(new Axis().setHasLines(true));

            // prepare preview data, is better to use separate deep copy for preview chart.
            // set color to grey to make preview area more visible.
            previewData = new ColumnChartData(data);
            for (Column column : previewData.getColumns()) {
                for (ColumnValue value : column.getValues()) {
                    value.setColor(Utils.DEFAULT_DARKEN_COLOR);
                }
            }

        }

        private void previewY() {
            Viewport tempViewport = new Viewport(chart.getMaximumViewport());
            float dy = tempViewport.height() / 4;
            tempViewport.inset(0, dy);
            previewChart.setCurrentViewport(tempViewport, true);
            previewChart.setZoomType(ZoomType.VERTICAL);
        }

        private void previewX(boolean animate) {
            Viewport tempViewport = new Viewport(chart.getMaximumViewport());
            float dx = tempViewport.width() / 4;
            tempViewport.inset(dx, 0);
            previewChart.setCurrentViewport(tempViewport, animate);
            previewChart.setZoomType(ZoomType.HORIZONTAL);
        }

        private void previewXY() {
            // Better to not modify viewport of any chart directly so create a copy.
            Viewport tempViewport = new Viewport(chart.getMaximumViewport());
            // Make temp viewport smaller.
            float dx = tempViewport.width() / 4;
            float dy = tempViewport.height() / 4;
            tempViewport.inset(dx, dy);
            previewChart.setCurrentViewport(tempViewport, true);
        }

        /**
         * Viewport listener for preview chart(lower one). in {@link #onViewportChanged(Viewport)} method change
         * viewport of upper chart.
         */
        private class ViewportListener implements ViewportChangeListener {

            @Override
            public void onViewportChanged(Viewport newViewport) {
                // don't use animation, it is unnecessary when using preview chart because usually viewport changes
                // happens to often.
                chart.setCurrentViewport(newViewport, false);
            }

        }

        private class GenerateDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {

                // it is executed on Background thread

                return params[0].GetArrayByID(0);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                generateData(jsonArray);

                chart.setColumnChartData(data);
                chart.setZoomEnabled(false);
                chart.setScrollEnabled(false);


                previewChart.setColumnChartData(previewData);
                previewChart.setViewportChangeListener(new ViewportListener());

                previewX(false);
            }
        }
    }
}
