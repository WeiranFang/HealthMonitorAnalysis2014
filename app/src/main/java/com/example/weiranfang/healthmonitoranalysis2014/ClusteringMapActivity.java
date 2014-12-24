package com.example.weiranfang.healthmonitoranalysis2014;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClusteringMapActivity extends BaseDemoActivity {
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.5017, -74.4481), 7));

        mClusterManager = new ClusterManager<MyItem>(this, getMap());



        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                double lat = position.latitude;
                double lng = position.longitude;
                String message = "latitude: " + lat + ", longitude: " + lng;
                Toast.makeText(ClusteringMapActivity.this, message, Toast.LENGTH_SHORT).show();
                 //RichToast.makeText(this, "My text", Toast.LENGH_SHORT, RichToast.RICHTOAST_BUDGET).show();
                return true;
            }
        });

        try {
            readItems();
        } catch (JSONException e) {
            //Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void readItems() throws JSONException {
        //InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        try {
            JSONArray jsonArray = new GenerateJSONArrayTask().execute(new JSONSender()).get();
            List<MyItem> items = new MyItemReader().read(jsonArray);

            for (MyItem item : items) {
                mClusterManager.addItem(item);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
/*

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        String firstName = cluster.getItems().iterator().next().getScreenName();
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();
        return true;
    }
*/


    private class GenerateJSONArrayTask extends AsyncTask<JSONSender,Long,JSONArray> {
        @Override
        protected JSONArray doInBackground(JSONSender... params) {
            // it is executed on Background thread
            return params[0].GetArrayByID(15);
        }
    }
}