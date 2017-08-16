package com.tianjin.MobileInspection.until;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 根据 uri 获取文件路径
 */
public class GetStringForUriUntil {
	
	public static String getString(Context context,Uri uri){
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String path = actualimagecursor.getString(actual_image_column_index);
		return path;
	}

}
