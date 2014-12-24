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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.PieChartView;

public class DemographyViewPagerChartsActivity extends ActionBarActivity implements ActionBar.TabListener {

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
                    return "Gender";
                case 1:
                    return "Age";

                /*
                case 2:
                    return "BubbleChart";
                case 3:
                    return "PreviewLineChart";
                case 4:
                    return "PieChart";
                */

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
            switch (sectionNum) {

                case 1:
                    PieChartView GenderPieChartView = new PieChartView(getActivity());

                    try {
                        PieChartData genderPieChartData = generateGenderPieChartData(
                                new GenerateJSONArrayTask().execute(new JSONSender()).get());

                        GenderPieChartView.setPieChartData(genderPieChartData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }



                    /** Note: Chart is within ViewPager so enable container scroll mode. **/
                    GenderPieChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
                    GenderPieChartView.setOnValueTouchListener(new ValueTouchListener());

                    layout.addView(GenderPieChartView);
                    break;

                case 2:
                    PieChartView AgePieChartView = new PieChartView(getActivity());
                    try {
                        PieChartData agePieChartData = generateAgePieChartData(
                                new GenerateJSONArrayTask().execute(new JSONSender()).get());
                        AgePieChartView.setPieChartData(agePieChartData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                    /** Note: Chart is within ViewPager so enable container scroll mode. **/
                    AgePieChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
                    AgePieChartView.setOnValueTouchListener(new ValueTouchListener());

                    layout.addView(AgePieChartView);
                    break;

            }

            return rootView;
        }



        private PieChartData generateGenderPieChartData(JSONArray jsonArray) throws JSONException {
            //int numValues = 2;

            List<ArcValue> values = new ArrayList<ArcValue>();

            for (int i = 0; i <= 1; ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int pieceData = jsonObject.getInt("demography_count");
                char[] pieceLabel = jsonObject.getString("gender_age").toCharArray();

                ArcValue arcValue = new ArcValue(pieceData, Utils.pickColor());
                arcValue.setLabel(pieceLabel);

                values.add(arcValue);
            }

            PieChartData data = new PieChartData(values);
            data.setHasLabels(true);

            return data;
        }


        private PieChartData generateAgePieChartData(JSONArray jsonArray) throws JSONException {
            //int numValues = 7;

            List<ArcValue> values = new ArrayList<ArcValue>();

            for (int i = 2; i <= 8; ++i) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int pieceData = jsonObject.getInt("demography_count");
                char[] pieceLabel = jsonObject.getString("gender_age").toCharArray();

                ArcValue arcValue = new ArcValue(pieceData, Utils.pickColor());
                arcValue.setLabel(pieceLabel);

                values.add(arcValue);
            }

            PieChartData data = new PieChartData(values);
            data.setHasLabels(true);

            return data;
        }


        private class GenerateJSONArrayTask extends AsyncTask<JSONSender,Long,JSONArray> {
            @Override
            protected JSONArray doInBackground(JSONSender... params) {

                // it is executed on Background thread

                return params[0].GetArrayByID(21);
            }
        }


        private class ValueTouchListener implements PieChartView.PieChartOnValueTouchListener {

            @Override
            public void onValueTouched(int selectedArc, ArcValue arcValue) {
                String Label = new String(arcValue.getLabel());
                int value = (int) arcValue.getValue();
                Toast.makeText(getActivity(), Label + ": " + value, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingTouched() {
                // TODO Auto-generated method stub

            }
        }
    }

}
