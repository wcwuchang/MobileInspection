/**
 * 
 */
package com.tianjin.MobileInspection.until;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 图片缓存机制
 * 
 */
public class BitmapLruCache implements ImageCache {

	private LruCache<String, Bitmap> cache;

	public BitmapLruCache() {
		cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		return cache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		cache.put(url, bitmap);
	}
}