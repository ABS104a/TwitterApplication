package com.abs104a.Utils;



import com.abs104a.storage.ImageCache;
import com.abs104a.storage.ImageDatabaseAdapter;
import com.abs104a.storage.MediaImageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * アイコンをバックグラウンドで取得するクラス
 * ビューとURLを受けとって画像取得、ビューにセットする
 */
public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

	// アイコンを表示するビュー
    private ImageView imageView;
    private TextView nameView;
    private String name;
	private final boolean isSave;
	private final boolean isRetweet;
	private final boolean isThumb;
	private final Context context;

	private static ActionQueue<ImageItem> queue = new ActionQueue<ImageItem>(5);
	private static int TASKNUM = queue.getMaxTask();

	/*
	public static int getTaskNum(){
		return queue.getTaskNum();
	}*/

	private ImageDatabaseAdapter dadapter;

    // コンストラクタ
    private ImageDownloadTask(Context context,ImageView imageView,TextView nameView,String name,boolean isSave,boolean isRetweet,boolean isThumb) {
        this.context = context;
    	this.imageView = imageView;
        this.nameView = nameView;
        this.name = name;
        this.isSave = isSave;
        this.isRetweet = isRetweet;
        this.isThumb = isThumb;
        dadapter = ImageDatabaseAdapter.getSingleton(context);
    }

    
    public static void getImageTask(Context context,String url,ImageView imageView,TextView nameView,String name,boolean isSave,boolean isRetweet,boolean isThumb){
    	if(queue.getTaskNum() <= TASKNUM){
    		new ImageDownloadTask(context,imageView, nameView, name, isSave,isRetweet,isThumb).execute(url);
    		//Log.v("ProceccNum", "" + queue.getTaskNum() + "/" + queue.size());
    	}else{
    		addImageTask(url,imageView,nameView,name,isSave,isRetweet,isThumb);
    	}
    }

    
    private static void addImageTask(String url,ImageView imageView,TextView nameView,String name,boolean isSave,boolean isRetweet,boolean isThumb){
    	ImageItem item = new ImageItem();
    	item.imageView = imageView;
    	item.nameView = nameView;
    	item.name = name;
    	item.isSave = isSave;
    	item.url = url;
    	item.isRetweet = isRetweet;
    	item.isThumb = isThumb;
    	//android.util.Log.v("in","in");
    	queue.offerItem(item);
    }

    @Override
	protected void onPreExecute() {
		super.onPreExecute();
		queue.addTaskNum();
	    //this.imageView.setImageResource(R.drawable.icon);
	}

    @Override
	protected void onCancelled() {
    	queue.minusTaskNum();
		super.onCancelled();
	}
	// バックグラウンドで実行する処理
    @Override
    protected Bitmap doInBackground(String... urls) {

    	Bitmap image;
    	if(isThumb){
    		image = MediaImageCache.getImage(urls[0]);
    		if (image == null) {
    			try{
    				//画像取得
    				image = ImageUtils.RadiusImage(HttpClient.getImage(urls[0]));
    				if(isSave){
    					MediaImageCache.setImage(urls[0], image);
    				}
    			}catch(Exception e){
    				if(isSave){
    					MediaImageCache.setImage(urls[0], null);
    				}
    				return null;
    			}
    		}
    	}else{
    		image = ImageCache.getImage(urls[0]);
    		if (image == null) {

    			try{
					//画像取得

					//dadapter .open();
					//ここでユーザー保存するべき？
					image = dadapter.getData(urls[0]);
					if(image == null){
						image = ImageUtils.RadiusImage(HttpClient.getImage(urls[0]));
						dadapter.insert(urls[0], image);
						//android.util.Log.v("ImageGet","get!");
					}
					
					//dadapter.close();
    				
    				if(isSave){
    					ImageCache.setImage(urls[0], image);
    				}
    			}catch(Exception e){
    				return null;
    			}
    		}
    	}
    	return image;
    }

    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Bitmap result) {
    	queue.minusTaskNum();
    	if(result != null){
    		if(!isRetweet && nameView.getText().toString().endsWith(name)){
    			this.imageView.setImageBitmap(result);
    			//android.util.Log.v("out","C");
    		}else if(isRetweet && nameView.getText().toString().startsWith("@"+name)){
    			this.imageView.setImageBitmap(result);
    			//android.util.Log.v("out","D");
    		}
    	}

    	if(queue.size() > 0){
    		ImageItem item = queue.pollItem();
    		getImageTask(context,item.url, item.imageView, item.nameView, item.name, item.isSave,item.isRetweet,item.isThumb);
    	}

    }
}