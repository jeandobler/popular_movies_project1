package com.popularmovies.android.popularmoviesp1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.android.popularmoviesp1.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    JSONObject movieDetail;
    ImageView mBackdropImageView;
    TextView mTitleTextView;
    TextView mSinopseTextView;
    TextView mReleaseDateTextView;
    TextView mVoteAverageTextView;

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mBackdropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mSinopseTextView = (TextView) findViewById(R.id.tv_sinopse);
        mVoteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {

            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                try {

                    movieDetail = new JSONObject(intentThatStartedThisActivity
                            .getStringExtra(Intent.EXTRA_TEXT));
                    mTitleTextView.setText(movieDetail.getString("original_title"));
                    mSinopseTextView.setText(movieDetail.getString("overview"));
                    String releaseText = getString(R.string.release_date) + " " + movieDetail.getString("release_date");
                    mReleaseDateTextView.setText(releaseText);
                    String noteText = getString(R.string.release_date) + " " + movieDetail.getString("vote_average");
                    mVoteAverageTextView.setText(noteText);

                    String urlImage = movieDetail.getString("poster_path");
                    Picasso.with(this)
                            .load(IMAGE_URL + urlImage)
                            .into(mBackdropImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = movieDetail.getString("overview");

                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_title)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
