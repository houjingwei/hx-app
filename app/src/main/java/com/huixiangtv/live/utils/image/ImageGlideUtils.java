package com.huixiangtv.live.utils.image;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huixiangtv.live.R;

/**
 * Created by Stone on 16/5/27.
 */
public class ImageGlideUtils {
    public static void display(Context context, String url, ImageView imageView) {
        display(context, url, imageView, null);
    }

    public static void display(Context context, String url, ImageView imageView, int width, int height) {
        StringBuilder sb = new StringBuilder();
        sb.append("_");
        sb.append(width);
        sb.append("x");
        sb.append(height);

        display(context, url, imageView, sb.toString());
    }

    public static void display(Context context, String url, ImageView imageView, String sizeSpec) {
        if(TextUtils.isEmpty(url)) {
            return;
        }

        StringBuilder sb = new StringBuilder(url);

        if(sizeSpec != null) {
            sb.append(sizeSpec);
        }

        Glide.with(context).load(sb.toString()).placeholder(R.drawable.v_default_lives).into(imageView);
    }

    public static void displayGif(Context context, String url, ImageView imageView){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(R.drawable.v_default_lives)
                .crossFade()
                .into(imageView);
    }

    public static void displayfsf(Context context, String url, ImageView imageView){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(R.drawable.v_default_lives)
                .crossFade()
                .into(imageView);
    }

    public static void displayPng(Context context, String url, ImageView imageView){
        if(TextUtils.isEmpty(url)){
            return;
        }
        Glide.with(context)
                .load(url)
                        //.placeholder(R.drawable.blank_default200)
                .into(imageView);
    }
}
