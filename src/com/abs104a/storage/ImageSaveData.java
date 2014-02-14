package com.abs104a.storage;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap.CompressFormat;

public class ImageSaveData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7203178871727926209L;
	public byte[] bitmap;
	public final long time;
	public final String url;


	public ImageSaveData(ImageData item) {
		time = item.time;
		url = item.url;
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			item.bitmap.compress(CompressFormat.PNG, 100, baos);
			bitmap = baos.toByteArray();
			baos.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
