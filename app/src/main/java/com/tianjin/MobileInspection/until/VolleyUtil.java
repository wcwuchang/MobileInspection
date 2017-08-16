package com.tianjin.MobileInspection.until;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;

/**
 * Volley使用
 */
public class VolleyUtil {
    private static RequestQueue mRequestQueue;

    public static void initialize(Context context) {
        if (mRequestQueue == null) {
            synchronized (VolleyUtil.class) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }

        mRequestQueue.start();
    }

    /**
     * 回去volley队列
     * @return
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            throw new RuntimeException("请先初始化mRequestQueue");
        return mRequestQueue;
    }


    /**
     * volley 控件 iv 根据 url 使用 imageloader 加载服务器上的图片
     * @param url
     * @param iv
     */
    public static void imageLoadingForServer(String url, ImageView iv){
        //此处传入的图片地址需要拼接指定的前缀
        StringBuffer sb=new StringBuffer();
        sb.append(ConnectionURL.HTTP_SERVER_IMAGE).append(url);
        ImageLoader imageLoader=new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapLruCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.bg_sign_up_prompt, R.drawable.img_sign_default);
        KLog.d(sb.toString());
        imageLoader.get(sb.toString(), listener);
    }

    /**
     * volley 控件 iv 根据 url 使用 imageloader 加载网络图片
     * @param url
     * @param iv
     */
    public static void imageLoading(String url, ImageView iv){
        //此处传入的图片地址需要拼接指定的前缀
        StringBuffer sb=new StringBuffer();
        sb.append(url);
        ImageLoader imageLoader=new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapLruCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.bg_sign_up_prompt, R.drawable.img_sign_default);
        KLog.d(sb.toString());
        imageLoader.get(sb.toString(), listener);
    }
}
