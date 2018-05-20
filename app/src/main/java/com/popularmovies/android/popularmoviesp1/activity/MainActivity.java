package com.popularmovies.android.popularmoviesp1.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popularmovies.android.popularmoviesp1.R;
import com.popularmovies.android.popularmoviesp1.adapter.MovieDbAdapter;
import com.popularmovies.android.popularmoviesp1.network.MovieNetwork;
import com.popularmovies.android.popularmoviesp1.network.util.ResultListJsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieDbAdapter.MovieDbAdapterOnClickHandler {

    public static final String SORT_POPULAR = "popular";
    public static final String SORT_RATED = "top_rated";
    public static final String SORT_UPCOMING = "upcoming";
    private RecyclerView mRecyclerView;
    private MovieDbAdapter mMovieDbAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int spanCount = 2 * getScreenOrientation();

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mMovieDbAdapter = new MovieDbAdapter(this);
        mRecyclerView.setAdapter(mMovieDbAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        loadMovieDataData("");
        super.onCreate(savedInstanceState);
    }

    public int getScreenOrientation() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_popular:
                loadMovieDataData(SORT_POPULAR);
                return true;
            case R.id.menu_top_rated:
                loadMovieDataData(SORT_RATED);
                return true;
            case R.id.menu_upcoming:
                loadMovieDataData(SORT_UPCOMING);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    private void loadMovieDataData(String string) {
        if (string.equals("")) {
            string = SORT_POPULAR;
        }
        showDataView();
//        String location = SunshinePreferences.getPreferredMovieLocation(this);
        new MakeMovieDbRequestTask().execute(string);
    }

    @Override
    public void onClick(JSONObject movieForDay) {

        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieForDay.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intentToStartDetailActivity,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intentToStartDetailActivity);
        }
    }


    public class MakeMovieDbRequestTask extends AsyncTask<String, Void, JSONArray> {


        @Override
        protected void onPreExecute() {


            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String url = params[0];
            URL movieRequestUrl = MovieNetwork.buildUrl(url);

            try {
                String jsonMovieResponse = MovieNetwork
                        .getResponseFromHttpUrl(movieRequestUrl);

                JSONArray simpleJsonMovieData = ResultListJsonUtil
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

//                String[] s = {jsonMovieResponse};
                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray movieData) {

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showDataView();
                mMovieDbAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }


}
