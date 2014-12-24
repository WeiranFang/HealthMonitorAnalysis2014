package com.example.weiranfang.healthmonitoranalysis2014;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by weiranfang on 11/13/14.
 */
public class MainListViewAdapter extends ArrayAdapter<String> {


    public MainListViewAdapter(Context context, String[] values) {
        super(context, R.layout.main_cell, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.main_cell, parent, false);

        String tvShow = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.main_cell_text_view);

        theTextView.setText(tvShow);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.main_cell_image_view);

        //theImageView.setImageResource(R.drawable.dot);

        return theView;
    }

}
