package hwang.daemin.kangbuk.common;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NoCache;
import com.navercorp.volleyextensions.cache.universalimageloader.disc.impl.UniversalUnlimitedDiscCache;
import com.navercorp.volleyextensions.cache.universalimageloader.memory.impl.UniversalLimitedAgeMemoryCache;
import com.navercorp.volleyextensions.cache.universalimageloader.memory.impl.UniversalLruMemoryCache;

import java.io.File;

/**
 * Created by hernia on 2015-06-20.
 */
public class MyVolley {
    private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
    private static final long DEFAULT_MAX_AGE = 60;
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;

    public static void init(Context context) {
        if (context == null)
            throw new NullPointerException("context must not be null.");

        Cache diskCache = getDefaultDiskCache(context);
        ImageCache memoryCache = getDefaultImageCache(context);
        requestQueue = new RequestQueue(diskCache, new BasicNetwork(
                new OkHttpStack()));

        imageLoader = new ImageLoader(requestQueue, memoryCache);

        requestQueue.start();
    }


    public static RequestQueue getRequestQueue() {
        if (requestQueue == null)
            throw new IllegalStateException("RequestQueue is not initialized.");
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        if (imageLoader == null)
            throw new IllegalStateException("ImageLoader is not initialized.");
        return imageLoader;
    }
    private static ImageCache getDefaultImageCache(Context context) {
        return new UniversalLimitedAgeMemoryCache(new UniversalLruMemoryCache(DEFAULT_CACHE_SIZE), DEFAULT_MAX_AGE);
    }

    private static Cache getDefaultDiskCache(Context context) {
        File cacheDir = getCacheDir(context);
        if (cacheDir == null) {
            return new NoCache();
        }

        return new UniversalUnlimitedDiscCache(cacheDir);
    }

    private static File getCacheDir(Context context) {
        File file = new File(context.getCacheDir().getPath());
        return file;
    }
}
