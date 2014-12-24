package com.example.weiranfang.healthmonitoranalysis2014;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by weiranfang on 12/2/14.
 */
public class LeaderBoardOverallAdapter extends BaseAdapter {


    private JSONArray dataArray;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public LeaderBoardOverallAdapter(JSONArray jsonArray, Activity a) {
        this.dataArray = jsonArray;
        this.activity = a;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //cet up convert view if it is null
        LeaderBoardCell cell;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.leader_board_overall_cell, null);
            cell = new LeaderBoardCell();

            cell.screenNameTextView = (TextView) convertView.findViewById(R.id.screen_name_text_view);
            cell.tweetNumTextView = (TextView) convertView.findViewById(R.id.tweet_num_text_view);
            cell.userImageView = (ImageView) convertView.findViewById(R.id.user_image_view);

            convertView.setTag(cell);
        } else {
            cell = (LeaderBoardCell) convertView.getTag();
        }

        //change the data of cell
        try {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);

            cell.screenNameTextView.setText(jsonObject.getString("screen_name"));
            cell.tweetNumTextView.setText("" + jsonObject.getInt("tweet_num"));
            new ImageLoadTask(jsonObject.getString("profile_image_url"), cell.userImageView).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class LeaderBoardCell {
        private TextView screenNameTextView;
        private TextView tweetNumTextView;
        private ImageView userImageView;
    }

    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

}
