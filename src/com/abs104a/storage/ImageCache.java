package com.abs104a.storage;

import java.util.Collection;

import android.graphics.Bitmap;

/*
 * イメージのキャッシュを行う
 * 毎回ネットから画像を取得するのは処理的に問題なので
 * キャッシュを作って保存する
 */
public class ImageCache {

    private static final int IMAGESIZE = 500;
    
	private static ConcurrentLRUCache<String, ImageData> cache = new ConcurrentLRUCache<String,ImageData>(IMAGESIZE);

    public static Bitmap getImage(String key) {
        if (cache.containsKey(key)) {
            //Log.d("cache", "cache hit!");
        	return cache.get(key).bitmap;
        }
        return null;
    }

    public static boolean isImage(String key) {
        return cache.containsKey(key);
    }

    public static Collection<ImageData> getDataArray(){
		return cache.values();

    }

    public static void setImage(String key, Bitmap image) {
        cache.put(key, new ImageData(image, System.currentTimeMillis(), key));
    }

	public static void setData(ImageData imageSaveData) {
		synchronized(cache){
			cache.put(imageSaveData.url,imageSaveData);
		}
	}

    /*
    public static void imageCacheClear(){
    	cache.clear();
    }*/
}