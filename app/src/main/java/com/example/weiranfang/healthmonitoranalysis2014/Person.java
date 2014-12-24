package com.example.weiranfang.healthmonitoranalysis2014;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {
    public final String name;
    //public final int profilePhoto;
    public final Bitmap profilePhoto;
    private final LatLng mPosition;

    public Person(LatLng position, String name, Bitmap picUrl) {
        this.name = name;
        profilePhoto = picUrl;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }



}
