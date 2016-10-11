package com.example.user.movie;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 10/7/2016.
 */
public class ArrayAdapterImage extends ArrayAdapter<ImageArray> {

    Context context;

ArrayAdapterImage(Activity context, List<ImageArray> imgarr)
{
    super(context,0,imgarr);
    this.context=context;

}
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageArray imgar=getItem(position);
        if(convertView==null) {
            LayoutInflater layoutInflater=(LayoutInflater) LayoutInflater
                    .from(context);
            convertView = layoutInflater.inflate(R.layout.movieimage, parent, false);
        }
        ImageView iv=(ImageView)convertView.findViewById(R.id.img_view);
        iv.setImageResource(imgar.image);
        TextView tv=(TextView)convertView.findViewById(R.id.txt_mov);
        tv.setText(imgar.name);
        final String MOVIE_BASE_URL = " http://image.tmdb.org/t/p/";
        final String SIZE = "w92";
        final String POSTER_PATH = "poster_path";
        String value;
        // ?????????????????????
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(SIZE)
                .appendPath(POSTER_PATH)
                .build();
        Picasso.with(getContext()).setLoggingEnabled(true);
        Picasso.with(getContext()).load(uri).into(iv);
        return  convertView;


    }


}
