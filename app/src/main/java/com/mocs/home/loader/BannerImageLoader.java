package com.mocs.home.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        String s=(String)path;
        if(s.startsWith("data:image/png;base64")){
            //base64
            byte[] decodeString= Base64.decode(s.split(",")[1],Base64.DEFAULT);
            Glide.with(context).load(decodeString).into(imageView);
        }else {
            //http
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
