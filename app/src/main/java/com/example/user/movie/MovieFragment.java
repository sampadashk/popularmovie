package com.example.user.movie;

import android.content.Context;
import android.content.Intent;
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movies = new ArrayList<ImageArray>();

        imageArrayAdapter = new ArrayAdapterImage(getActivity(), movies);
        gridview = (GridView) rootView.findViewById(R.id.grd);
        gridview.setAdapter(imageArrayAdapter);
        gridview.setVisibility(View.VISIBLE);
       /* gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            // These two need to be declared outside the try/catch
// so that they can be closed in the finally bloc

            public void onItemClick(AdapterView<?> adapterview,View view,int i,long l)
            {
               ImageArray movieval=imageArrayAdapter.getItem(i);

                Bundle bundle=new Bundle();
                bundle.putString("title",movieval.title);
                bundle.putString("release",movieval.release);
                bundle.putString("thumb",movieval.thumb);
                bundle.putString("rating",movieval.rating);
                bundle.putString("plot",movieval.plot);

               // Intent intent=new Intent(getActivity(),DetailActivity.class).putExtras(bundle);
                //startActivity(intent);

            }



        });
        */






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
        ImageArray[] moviesend;


        private ImageArray[] getmovieDataFromJson(String forecastJsonStr)
                throws JSONException {
            final String result = "results";
            final String poster = "poster_path";
            final String TITLE = "title";
            final String origin_name="original_title";
            final String thumb="backdrop_path";
            final String synopsis="overview";
            final String rating="vote_average";
            final String release="release_date";
            final String popular = "popularity";

            JSONObject jsobject = new JSONObject(forecastJsonStr);
            JSONArray jsarray = jsobject.getJSONArray(result);
            int n = jsarray.length();
            movieArr=new ImageArray[n];
            moviesend=new ImageArray[n];

            String[] add = new String[n];
            String movieName;
            String posterpath;
            String movieTitle;
            String releaseDate;
            String ratings;
            String plot;
            String thumbs;

            //ImageView imageView=(ImageView) view.findViewById(R.id.img_view);
            try {
                for (int i = 0; i < jsarray.length(); i++) {
                    String moviePosters;

                    Image img;
                    JSONObject popmovie = jsarray.getJSONObject(i);
                    movieName = popmovie.getString(TITLE);
                    posterpath = popmovie.getString(poster);
                    movieTitle=popmovie.getString(origin_name);
                    releaseDate=popmovie.getString(release);
                    ratings=popmovie.getString(rating);
                    plot=popmovie.getString(synopsis);
                    thumbs=popmovie.getString(thumb);
                    moviePosters = "https://image.tmdb.org/t/p/w342" + posterpath;

                    movieArr[i] = new ImageArray(movieName, moviePosters);
                    Log.d("tag_ch",movieArr[i].name);
                    //moviesend[i]=new ImageArray(movieTitle,releaseDate,thumbs,ratings,plot);


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
            String appKey = "0d8834b1d5d00841ba937c9185b4b03d";
            try {
                final String QUERY_PARAM = "sort_by";

                String format = "json";
                final String APPID_PARAM = "api_key";

                String add = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";

                Uri ur = Uri.parse(add).buildUpon().appendQueryParameter(APPID_PARAM, appKey).build();
                URL url = new URL(ur.toString());
                Log.d("check_u",ur.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                InputStream ip = con.getInputStream();
                if (ip == null) {
                    // Nothing to do.
                    forecastJsonStr=null;
                }
                br = new BufferedReader(new InputStreamReader(ip));

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
                    forecastJsonStr=null;
                }
                forecastJsonStr = buffer.toString();
                Log.d("tag_check", "forecast json" + forecastJsonStr);


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
