package com.tianjin.MobileInspection.until;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.ImageItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BitmapUtil {
	public static int max = 0;
	public static BitmapDescriptor bmArrowPoint = null;
	public static BitmapDescriptor bmStart = null;
	public static BitmapDescriptor bmEnd = null;
	public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //选择的图片的临时列表

	public static void init() {
		bmArrowPoint = BitmapDescriptorFactory.fromResource(R.mipmap.icon_point);
		bmStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_qidian);
		bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_zhongdian);
	}

	/**
	 * 加载本地图片
	 * @param path
	 * @return
	 * @throws IOException
     */
	public static Bitmap revitionImageSize(String path) throws IOException {
		if(path==null) return null;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
