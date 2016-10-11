package com.example.user.movie;

import android.media.Image;

/**
 * Created by user on 10/8/2016.
 */
public class ImageArray {

   public String link;
    int image;
    String name;
    ImageArray(String name,int image)
    {
        this.image=image;
        this.name=name;
    }
    ImageArray(String name,String link)
    {
      this.name=name;
        this.link=link;
    }

}
