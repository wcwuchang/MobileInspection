package com.tianjin.MobileInspection.entity;

import android.graphics.Bitmap;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.until.BitmapUtil;

import java.io.IOException;
import java.io.Serializable;


public class ImageItem implements Serializable {

	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private Bitmap bitmap;
	private String type;
	private String imageName;
	private int showId;
	public boolean isdelete = false;
	public boolean isadd=false;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isadd() {
		return isadd;
	}

	public void setIsadd(boolean isadd) {
		this.isadd = isadd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isDelete() {
		return isdelete;
	}
	public void setDelete(boolean isSelected) {
		this.isdelete = isSelected;
	}
	public Bitmap getBitmap() {
		if(bitmap == null ){
			try {
				bitmap = BitmapUtil.revitionImageSize(imagePath);
			} catch (IOException e) {
				KLog.d(e.getMessage());
			}
		}
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
	
}
