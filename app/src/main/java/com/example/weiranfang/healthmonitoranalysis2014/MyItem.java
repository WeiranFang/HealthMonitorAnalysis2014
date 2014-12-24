package com.example.weiranfang.healthmonitoranalysis2014;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String screenName;
    private String exerciseType;
    private String exerciseTime;

//    public MyItem(double lat, double lng) {
//        mPosition = new LatLng(lat, lng);
//    }

    public MyItem(double lat, double lng, String name, String type, String time) {
        mPosition = new LatLng(lat, lng);
        screenName = name;
        exerciseType = type;
        exerciseTime = time;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public String getExerciseTime() {
        return exerciseTime;
    }
}
