package com.example.weiranfang.healthmonitoranalysis2014;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ETimeStateViewPagerChartsActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too
     * memory intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_charts);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Mean";
                case 1:
                    return "Max";

            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public final static String[] states = new String[] { "AK", "AL", "AR", "AZ", "CA", "CO", "CT",
                "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME",
                "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH",
                "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV",
                "WY"};

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_pager_charts, container, false);
            RelativeLayout layout = (RelativeLayout) rootView;
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);

            ColumnChartView columnChartView = new ColumnChartView(getActivity());
            try {
                ColumnChartData meanColumnChartData = generateColumnChartData(sectionNum,
                        new GenerateJSONArrayTask().execute(new JSONSender()).get());
                columnChartView.setColumnChartData(meanColumnChartData);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            columnChartView.setZoomType(ZoomType.HORIZONTAL);
            //columnChartView.setMaximumViewport(new Viewport( -1, 400, 7, 0));


            /** Note: Chart is within ViewPager so enable container scroll mode. **/
            columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

            layout.addView(columnChartView);



            return rootView;
        }
/*

        private LineChartData generateLineChartData() {
            int numValues = 20;

            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, (float) Math.random() * 100f));
            }

            Line line = new Line(values);
            line.setColor(Utils.COLOR_GREEN);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            LineChartData data = new LineChartData(lines);
            data.setAxisXBottom(new Axis().setName("Axis X"));
            data.setAxisYLeft(new Axis().setName("Axis Y").setHasLines(true));
            return data;

        }
*/

        private ColumnChartData generateColumnChartData(int index, JSONArray jsonArray) throws JSONException {
            //int numSubcolumns = 1;
            int numColumns = jsonArray.length();
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<ColumnValue> values;
            List<AxisValue> axisValues = new ArrayList<AxisValue>();

            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<ColumnValue>();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(index == 0) {
                    values.add(new ColumnValue(jsonObject.getInt("mean"), Utils.pickColor()));
                } else {
                    values.add(new ColumnValue(jsonObject.getInt("max"), Utils.pickColor()));
                }

                axisValues.add(new AxisValue(i, states[i].toCharArray()));
                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            ColumnChartData data = new ColumnChartData(columns);

            data.setAxisXBottom(new Axis(axisValues).setMaxLabelChars(10));
            data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
            return data;

        }
/*

        private BubbleChartData generateBubbleChartData() {
            int numBubbles = 10;

            List<BubbleValue> values = new ArrayList<BubbleValue>();
            for (int i = 0; i < numBubbles; ++i) {
                BubbleValue value = new BubbleValue(i, (float) Math.random() * 100, (float) Math.random() * 1000);
                value.setColor(Utils.pickColor());
                values.add(value);
            }

            BubbleChartData data = new BubbleChartData(values);

            data.setAxisXBottom(new Axis().setName("Axis X"));
            data.setAxisYLeft(new Axis().setName("Axis Y").setHasLines(true));
            return data;
        }

        private LineChartData generatePreviewLineChartData() {
            int numValues = 50;

            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new PointValue(i, (float) Math.random() * 100f));
            }

            Line line = new Line(values);
            line.setColor(Utils.DEFAULT_DARKEN_COLOR);
            line.setHasPoints(false);// too many values so don't draw points.

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            LineChartData data = new LineChartData(lines);
            data.setAxisXBottom(new Axis());
            data.setAxisYLeft(new Axis().setHasLines(true));

            return data;

        }

        private PieChartData generatePieChartData() {
            int numValues = 6;

            List<ArcValue> values = new ArrayList<ArcValue>();
            for (int i = 0; i < numValues; ++i) {
                values.add(new ArcValue((float) Math.random() * 30 + 15, Utils.pickColor()));
            }

            PieChartData data = new PieChartData(values);
            return data;
        }

*/

        private class GenerateJSONArrayTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {
                // it is executed on Background thread
                return params[0].GetArrayByID(11);
            }
        }

    }

}
