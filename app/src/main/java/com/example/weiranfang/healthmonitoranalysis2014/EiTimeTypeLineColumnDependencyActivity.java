package com.example.weiranfang.healthmonitoranalysis2014;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class EiTimeTypeLineColumnDependencyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_column_dependency);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public final static String[] types = new String[] { "Running", "Cycling", "Swimming", "Basketball", "Volleyball",
                "Tennis", "Football",};

        public final static String[] clocks = new String[] { "0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00",
                "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00", };

        private LineChartView chartTop;
        private ColumnChartView chartBottom;

        private LineChartData lineData;
        private ColumnChartData columnData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_line_column_dependency, container, false);

            // *** TOP LINE CHART ***
            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);

            // Generate and set data for line chart
            generateInitialLineData();

            // *** BOTTOM COLUMN CHART ***

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);

            new GenerateColumnDataTask().execute(new JSONSender());

            return rootView;
        }

        private void generateColumnData(JSONArray jsonArray) {

            //int numSubcolumns = 1;
            int numColumns = types.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<ColumnValue> values;
            for (int i = 0; i < numColumns; ++i) {


                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int singleColumnData = jsonObject.getInt("exercise_count");

                    values = new ArrayList<ColumnValue>();

                    values.add(new ColumnValue(singleColumnData, Utils.pickColor()));
                    axisValues.add(new AxisValue(i, types[i].toCharArray()));

                    columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5));

            chartBottom.setColumnChartData(columnData);

            // Set value touch listener that will trigger changes for chartTop.
            chartBottom.setOnValueTouchListener(new ValueTouchListener());

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomType(ZoomType.HORIZONTAL);

            // chartBottom.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // SelectedValue sv = chartBottom.getSelectedValue();
            // if (!sv.isSet()) {
            // generateInitialLineData();
            // }
            //
            // }
            // });

        }

        /**
         * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
         * will select value on column chart.
         */
        private void generateInitialLineData() {
            int numValues = clocks.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, 0));
                axisValues.add(new AxisValue(i, clocks[i].toCharArray()));
            }

            Line line = new Line(values);
            line.setColor(Utils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setMaxLabelChars(10));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));


            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 2500, 23, 0);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v, false);
            chartTop.setZoomEnabled(true);
            chartTop.setZoomType(ZoomType.VERTICAL);

        }

        private void generateLineData(int color, float range) {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);
            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                value.setTarget(value.getX(), (float) Math.random() * range);
            }

            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(300);
        }

        private void generateRunningLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 3000, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }

        private void generateCyclingLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24 * 2;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 100, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }

        private void generateSwimmingLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24 * 3;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 100, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }

        private void generateBasketballLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24 * 4;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 100, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }

        private void generateVolleyballLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24 * 5;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 100, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }

        private void generateTennisLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24 * 6;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 100, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }


        private void generateFootballLineData(JSONArray jsonArray) throws JSONException {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();

            // Modify data targets
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            //line.setColor(color);

            List<PointValue> pointValues = line.getValues();

            int startIndex = 24 * 7;
            int endIndex = startIndex + 23;

            int j = 0;

            for(int i = startIndex; i<= endIndex; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int value = jsonObject.getInt("time_count");
                PointValue pointValue = pointValues.get(j++);
                pointValue.setTarget(pointValue.getX(), value);
            }

            //Viewport v = new Viewport(0, 500, 23, 0);
            //chartTop.setMaximumViewport(v);

            chartTop.startDataAnimation(300);
        }

        private class ValueTouchListener implements ColumnChartView.ColumnChartOnValueTouchListener {

            @Override
            public void onValueTouched(int selectedLine, int selectedValue, ColumnValue value) {


                //generateLineData(value.getColor(), 100);

                if(selectedLine == 0) {
                    //Running

                    new GenerateRunningLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                } else if(selectedLine == 1) {
                    //Cycling

                    new GenerateCyclingLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                } else if(selectedLine == 2) {
                    //Swimming

                    new GenerateSwimmingLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                } else if(selectedLine == 3) {
                    //Basketball

                    new GenerateBasketballLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                } else if(selectedLine == 4) {
                    //Volleyball

                    new GenerateVolleyballLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                } else if(selectedLine == 5) {
                    //Tennis

                    new GenerateTennisLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                } else if(selectedLine == 6) {
                    //Football

                    new GenerateFootballLineDataTask().execute(new JSONSender());
                    Line line = lineData.getLines().get(0);
                    line.setColor(value.getColor());

                }


            }

            @Override
            public void onNothingTouched() {

                generateLineData(Utils.COLOR_GREEN, 0);

            }
        }

        private class GenerateColumnDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(1);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                generateColumnData(jsonArray);
            }
        }


        private class GenerateRunningLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateRunningLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        private class GenerateCyclingLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateCyclingLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        private class GenerateSwimmingLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateSwimmingLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private class GenerateBasketballLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateBasketballLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private class GenerateVolleyballLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateVolleyballLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private class GenerateTennisLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateTennisLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private class GenerateFootballLineDataTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                return params[0].GetArrayByID(2);
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                try {
                    generateFootballLineData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
