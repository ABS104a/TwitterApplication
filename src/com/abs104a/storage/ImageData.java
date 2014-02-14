package com.abs104a.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageData{
	public final Bitmap bitmap;
	public final long time;
	public final String url;

	public ImageData(Bitmap bitmap,long time,String url){
		this.bitmap = bitmap;
		this.time = time;
		this.url = url;
	}

	public ImageData(ImageSaveData item) {
		this.time = item.time;
		this.url = item.url;
		this.bitmap = BitmapFactory.decodeByteArray(item.bitmap, 0, item.bitmap.length);
	}
}
