package com.popularmovies.android.popularmoviesp1.network;

import android.net.Uri;

import com.popularmovies.android.popularmoviesp1.activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MovieNetwork {


    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3";

    final static String PARAM_QUERY = "q";
    final static String POPULAR_URL = "/movie/popular";
    final static String TOPDATED_URL = "/movie/top_rated";
    final static String UPCOMING_URL = "/movie/upcoming";

    final static String PARAM_KEY = "api_key";

    //TODO ADD YOUR KEY HERE
    final static String API_KEY_V3 = "";

    static Uri builtUri;


    public static URL buildUrl(String param) {

        String urlBuildedString = "";
        switch (param) {
            case MainActivity.SORT_POPULAR:
                urlBuildedString = MOVIEDB_BASE_URL + POPULAR_URL;
                break;
            case MainActivity.SORT_RATED:
                urlBuildedString = MOVIEDB_BASE_URL + TOPDATED_URL;
                break;
            case MainActivity.SORT_UPCOMING:
                urlBuildedString = MOVIEDB_BASE_URL + UPCOMING_URL;
                break;
        }


        builtUri = Uri.parse(urlBuildedString).buildUpon()
                .appendQueryParameter(PARAM_KEY, API_KEY_V3).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
