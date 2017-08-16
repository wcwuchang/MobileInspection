package com.tianjin.MobileInspection.until;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.module.FileUploadModule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    
    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     * @param c
     * @param fileName 文件名称
     * @param bitmap 图片
     * @return
     */
	public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
		byte[] bytes = bitmapToBytes(bitmap);
		return saveFile(c, filePath, fileName, bytes);
	}
	
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
		String fileFullName = "";
		FileOutputStream fos = null;
		String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
				.format(new Date());
		try {
			String suffix = "";
			if (filePath == null || filePath.trim().length() == 0) {
				filePath = MyApplication.IMAGEFILEPATH;
			}
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			File fullFile = new File(filePath, fileName + suffix);
			fileFullName = fullFile.getPath();
			fos = new FileOutputStream(new File(filePath, fileName + suffix));
			fos.write(bytes);
		} catch (Exception e) {
			fileFullName = "";
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					fileFullName = "";
				}
			}
		}
		return fileFullName;
	}

	//获取文件名
	public static String getFileNameForUrl(String url){
		String[] str=url.split("\\/");
		return str[str.length-1];
	}

	/**
	 * 保存网络图片，并返回本地保存路径
	 * @retu
	 *
	 * */
	public static String saveIntenetImage(Context context,String uri){
		String name= FileUtil.getFileNameForUrl(uri);
		File file=new File(MyApplication.IMAGEFILEPATH,name);
		if(file.exists()){
			return file.getPath();
		}else {
			Bitmap bitmap= FileUploadModule.getBitmapFromUrl(uri);
			if(bitmap==null){
				return null;
			}
			return FileUtil.saveFile(context,MyApplication.IMAGEFILEPATH,FileUtil.getFileNameForUrl(uri),bitmap);
		}
	}

	/**
	 * 创建文件夹
	 */
	public static void creatFile(){
		String path = MyApplication.CATCH_FILE;
		File file=new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagepath = MyApplication.IMAGEFILEPATH;
		File image=new File(imagepath);
		if (!image.exists()) {
			image.mkdirs();
		}
		String cache = MyApplication.ICACHE_PATH;
		File cachee=new File(cache);
		if (!cachee.exists()) {
			cachee.mkdirs();
		}
		String downpath = MyApplication.DOWNLOAD_PATH;
		File down=new File(downpath);
		if (!down.exists()) {
			down.mkdirs();
		}
		String headpath = MyApplication.CONTACT_PATH;
		File head=new File(downpath);
		if (!head.exists()) {
			head.mkdirs();
		}
	}

	/**
	 * 情况所以缓存文件
	 */
	public static void clearFile(File file){
		if (file.exists()) {
			File[] files=file.listFiles();
			for(File file1:files){
				if(file1.isDirectory()){
					clearFile(file1);
				}else {
					file1.delete();
				}
			}
		}
	}

	/**
	 * 判断该文件夹中是否存在文件（是否清理干净）
	 * @param file
	 * @return
     */
	public static boolean isClearSuccess(File file){
		if (file.exists()) {
			File[] files=file.listFiles();
			for(File file1:files){
				if(file1.isDirectory()){
					isClearSuccess(file1);
				}else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 调用系统打开文件
	 * @param context
	 * @param path
     */
	public static void openFile(Context context,String path){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		//获取文件file的MIME类型
		String type = getFileType(path);
		File file=new File(path);
		//设置intent的data和Type属性。
		intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
		//跳转
		context.startActivity(intent);
	}

	/**
	 * 根据文件类型获取intent
	 * @param path
	 * @return
     */
	private static String getFileType(String path){
		String type="*/*";
		String fName = path;
		//获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
    /* 获取文件的后缀名 */
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end=="")return type;
		//在MIME和文件类型的匹配表中找到对应的MIME类型。
		for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable
			if(end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	private static final String[][] MIME_MapTable={
			//{后缀名， MIME类型}
			{".3gp",    "video/3gpp"},
			{".apk",    "application/vnd.android.package-archive"},
			{".asf",    "video/x-ms-asf"},
			{".avi",    "video/x-msvideo"},
			{".bin",    "application/octet-stream"},
			{".bmp",    "image/bmp"},
			{".c",  	"text/plain"},
			{".class",  "application/octet-stream"},
			{".conf",   "text/plain"},
			{".cpp",    "text/plain"},
			{".doc",    "application/msword"},
			{".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
			{".xls",    "application/vnd.ms-excel"},
			{".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
			{".exe",    "application/octet-stream"},
			{".gif",    "image/gif"},
			{".gtar",   "application/x-gtar"},
			{".gz", 	"application/x-gzip"},
			{".h",  	"text/plain"},
			{".htm",    "text/html"},
			{".html",   "text/html"},
			{".jar",    "application/java-archive"},
			{".java",   "text/plain"},
			{".jpeg",   "image/jpeg"},
			{".jpg",    "image/jpeg"},
			{".js", 	"application/x-javascript"},
			{".log",    "text/plain"},
			{".m3u",    "audio/x-mpegurl"},
			{".m4a",    "audio/mp4a-latm"},
			{".m4b",    "audio/mp4a-latm"},
			{".m4p",    "audio/mp4a-latm"},
			{".m4u",    "video/vnd.mpegurl"},
			{".m4v",    "video/x-m4v"},
			{".mov",    "video/quicktime"},
			{".mp2",    "audio/x-mpeg"},
			{".mp3",    "audio/x-mpeg"},
			{".mp4",    "video/mp4"},
			{".mpc",    "application/vnd.mpohun.certificate"},
			{".mpe",    "video/mpeg"},
			{".mpeg",   "video/mpeg"},
			{".mpg",    "video/mpeg"},
			{".mpg4",   "video/mp4"},
			{".mpga",   "audio/mpeg"},
			{".msg",    "application/vnd.ms-outlook"},
			{".ogg",    "audio/ogg"},
			{".pdf",    "application/pdf"},
			{".png",    "image/png"},
			{".pps",    "application/vnd.ms-powerpoint"},
			{".ppt",    "application/vnd.ms-powerpoint"},
			{".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
			{".prop",   "text/plain"},
			{".rc",     "text/plain"},
			{".rmvb",   "audio/x-pn-realaudio"},
			{".rtf",    "application/rtf"},
			{".sh",     "text/plain"},
			{".tar",    "application/x-tar"},
			{".tgz",    "application/x-compressed"},
			{".txt",    "text/plain"},
			{".wav",    "audio/x-wav"},
			{".wma",    "audio/x-ms-wma"},
			{".wmv",    "audio/x-ms-wmv"},
			{".wps",    "application/vnd.ms-works"},
			{".xml",    "text/plain"},
			{".z",  	"application/x-compress"},
			{".zip",    "application/x-zip-compressed"},
			{"",        "*/*"}
	};

	/**
	 * 保存信息到本地
	 * @param msg
     */
	public static void saveLocationInfo(String msg){
		File file=new File(MyApplication.ICACHE_PATH,MyApplication.getStringSharedPreferences("name","")+".txt");
		FileOutputStream outputStream=null;
		try {
			String content="\r"+msg;
			if(!file.exists()) file.createNewFile();
			outputStream=new FileOutputStream(file,true);
			outputStream.write(content.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}



	public static String compressImage(String filePath, String targetPath, int quality)  {
		File file=new File(filePath);
		try {
			long size=getFileSize(file);
			int scale=1;
			if(size>=1024*4){
				scale=8;
			}else if(size>=1024*2){
				scale=4;
			}else if(size>=1024){
				scale=2;
			}
			Bitmap bm = getSmallBitmap(scale,filePath);//获取一定尺寸的图片
                int degree = readPictureDegree(filePath);//获取相片拍摄角度
                if (degree != 0) {//旋转照片角度，防止头像横着显示
                    bm = rotateBitmap(bm, degree);
                }
                File outputFile = new File(targetPath);
                try {
                    if (!outputFile.exists()) {
                        outputFile.getParentFile().mkdirs();
                    } else {
                        outputFile.delete();
                    }
                    FileOutputStream out = new FileOutputStream(outputFile);
                    bm.compress(CompressFormat.JPEG, quality, out);
                } catch (Exception e) {
                }
                return outputFile.getPath();
		} catch (Exception e) {
			return filePath;
		}
	}

	/**
	 * 根据路径获得图片信息并按比例压缩，返回bitmap
	 */
	public static Bitmap getSmallBitmap(int scale,String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
		Bitmap bitmap=BitmapFactory.decodeFile(filePath);
		int w=bitmap.getWidth()/scale;
		int h=bitmap.getHeight()/scale;
		BitmapFactory.decodeFile(filePath, options);
		// 计算缩放比
		options.inSampleSize = calculateInSampleSize(options, w, h);
		// 完整解析图片返回bitmap
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}


	/**
	 * 获取照片角度
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转照片
	 * @param bitmap
	 * @param degress
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap,int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 获取指定文件大小
	 * @param file
	 * @return Kb
	 * @throws Exception 　　
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			Log.e("获取文件大小", "文件不存在!");
		}
		return size/1024;
	}


}
