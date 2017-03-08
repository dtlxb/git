package com.hply.imagepicker.loader;

import android.app.Activity;
import android.widget.ImageView;

public interface ImageLoader{

    void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

    void clearMemoryCache();
}
