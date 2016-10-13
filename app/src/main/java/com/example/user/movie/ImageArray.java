package com.example.user.movie;

import android.media.Image;

/**
 * Created by user on 10/8/2016.
 */
public class ImageArray {

   public String link;
    int image;
    String name;
    String title;
    String release;
    String thumb;
    String rating;
    String plot;
    ImageArray(String name,int image)
    {
        this.image=image;
        this.name=name;
    }
    ImageArray(String name,String link,String title,String release,String thumb,String rating,String plot)
    {
      this.name=name;
        this.link=link;
        this.title=title;
        this.release=release;
        this.thumb=thumb;
        this.rating=rating;
        this.plot=plot;

    }
  /* ImageArray(String title,String release,String thumb,String rating,String plot)
    {
        this.title=title;
        this.release=release;
        this.thumb=thumb;
        this.rating=rating;
        this.plot=plot;
    }
    */


}
