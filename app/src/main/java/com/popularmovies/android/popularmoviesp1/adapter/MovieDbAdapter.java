package com.popularmovies.android.popularmoviesp1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.android.popularmoviesp1.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDbAdapter extends RecyclerView.Adapter<MovieDbAdapter.MovieDbAdapterViewHolder> {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final MovieDbAdapterOnClickHandler mClickHandler;
    Context context;
    private JSONArray mMovieData;


    public MovieDbAdapter(MovieDbAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieDbAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieDbAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDbAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieForThisDay = null;
        try {
            movieForThisDay = mMovieData.getJSONObject(position).getString("title");
            String urlImage = mMovieData.getJSONObject(position).getString("poster_path");
            Context context = movieAdapterViewHolder.mPoster.getContext();
            Picasso.with(context).load(IMAGE_URL + urlImage).into(movieAdapterViewHolder.mPoster);
//            w780
        } catch (JSONException e) {
            e.printStackTrace();
        }

        movieAdapterViewHolder.mMovieTextView.setText(movieForThisDay);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length();
    }

    public void setMovieData(JSONArray movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public interface MovieDbAdapterOnClickHandler {
        void onClick(JSONObject movieForDay);
    }

    public class MovieDbAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mMovieTextView;
        public final ImageView mPoster;

        public MovieDbAdapterViewHolder(View view) {
            super(view);
            mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);
            mPoster = (ImageView) view.findViewById(R.id.iv_poster);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            JSONObject movieForDay = null;
            try {
                movieForDay = mMovieData.getJSONObject(adapterPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mClickHandler.onClick(movieForDay);
        }
    }
}

