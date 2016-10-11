package com.example.user.movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ArrayAdapterImage imageArrayAdapter;
    private List<ImageArray> movies;
    GridView gridview;


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        movies = new ArrayList<ImageArray>();

        imageArrayAdapter = new ArrayAdapterImage(getActivity(), movies);
        gridview = (GridView) rootView.findViewById(R.id.grd);
        gridview.setAdapter(imageArrayAdapter);
        gridview.setVisibility(View.VISIBLE);


        return rootView;
    }

    public void onStart() {
        super.onStart();
        displayMovie();
    }

    private void displayMovie() {
        FetchMovie ft = new FetchMovie();
        ft.execute();
    }

    class FetchMovie extends AsyncTask<String, Void, ImageArray[]> {

        ImageArray[] movieArr ;


        private ImageArray[] getmovieDataFromJson(String forecastJsonStr)
                throws JSONException {
            final String result = "results";
            final String poster = "poster_path";
            final String TITLE = "title";
            final String popular = "popularity";
            final String rating = "vote_average";
            JSONObject jsobject = new JSONObject(forecastJsonStr);
            JSONArray jsarray = jsobject.getJSONArray(result);
            int n = jsarray.length();
            movieArr=new ImageArray[n];

            String[] add = new String[n];
            String movieName;
            String posterpath;

            //ImageView imageView=(ImageView) view.findViewById(R.id.img_view);
            try {
                for (int i = 0; i < jsarray.length(); i++) {
                    String moviePosters;
                    String movieTitles;
                    Image img;
                    JSONObject popmovie = jsarray.getJSONObject(i);
                    movieName = popmovie.getString(TITLE);
                    posterpath = popmovie.getString(poster);
                    moviePosters = "https://image.tmdb.org/t/p/" + posterpath;
                    movieTitles = movieName;
                    movieArr[i] = new ImageArray(movieTitles, moviePosters);


                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return movieArr;


        }

        protected ImageArray[] doInBackground(String... params) {
            HttpURLConnection con = null;
            BufferedReader br = null;
            String forecastJsonStr = null;
            String popularityval = "popularity.desc";
            String ampersand = "&";
            try {
                final String QUERY_PARAM = "sort_by";

                String format = "json";

                String add = "http://api.themoviedb.org/3/discover/movie?api_key=0d8834b1d5d00841ba937c9185b4b03d";

                Uri ur = Uri.parse(add).buildUpon().appendPath(ampersand).appendQueryParameter(QUERY_PARAM, popularityval).build();
                URL url = new URL(ur.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("get");
                con.connect();
                InputStream ip = con.getInputStream();
                br = new BufferedReader(new InputStreamReader(ip));
                if (ip == null) {
                    // Nothing to do.
                    return null;
                }
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v("tag_g", "forecast json" + forecastJsonStr);


            } catch (IOException ie) {
                return null;
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getmovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e("log_t", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ImageArray[] result) {

            if (result != null) {

                imageArrayAdapter.addAll(result);
            }

        }


        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */

    }
}
