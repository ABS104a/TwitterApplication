package com.abs104a.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.abs104a.Utils.RegexUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/*
 *Class DatabaseAdapter
 *
 *ユーザーアイコンを管理するためのデーターベースアダプター
 *読み込みと書き込みを行う．
 */
public final class ImageDatabaseAdapter  {
    

	/** データベースを置くSD上のフォルダのフルパス */
    private static final String DB_DIRECTORY =
        Environment.getExternalStorageDirectory() + "/TwitterLook/";
    
    private static final String INDB_NAME = 
    		"user_icon.db";
     
    /** データベースファイルのフルパス */
    private static final String DB_NAME =
        DB_DIRECTORY + INDB_NAME;
    
     
     
    /** テーブル作成のSQL文 */
    private static final String CREATE_TABLE =
        "create table if not exists IconList (" +
        "_id integer primary key autoincrement, " +
        "Url text, " +
        "Date integer, " +
        "Icon blob" +
        ");";
     
	//データベース
    private SQLiteDatabase db = null;

	//アプリのコンテキスト
	private final Context context;

	//データベースヘルパー（オープン用
	private DatabaseHelper dbHelper = null;
	
	//Singleton用
	private static ImageDatabaseAdapter imageAdapter = null;
    
	//コンストラクタ
    private ImageDatabaseAdapter(Context context){
    	this.context = context;
    	//DBを非同期で読みに行く
    	readDB();
    	
    }
    
    private void readDB(){
    	new OpenDataBaseTask().execute();
    }
    
    private class OpenDataBaseTask extends AsyncTask<Void, Void, SQLiteDatabase> {
        protected SQLiteDatabase doInBackground(Void... args) {
        	//SDカードを優先で書き込み可能か確認，読み込み出来ない場合は内部メモリに
        	SQLiteDatabase db = null;
        	if(isSdCanWrite() && RegexUtil.getExternalUseableMem() > 30){
        		SQLiteDatabase dba = getDatabase_SD();
        		if(dba == null)
        			dba = getDatabase_Storage();
        		db = dba;
        	}else if(RegexUtil.getInternalUseableMem() > 30){
        		db = getDatabase_Storage();
        	}else{
        		db = null;
        	}	
        	if(db != null)
        		oldDelete(context,db);
        	return db;
        }

        protected void onPostExecute(SQLiteDatabase result) {
        	//DBの読み込みに失敗した場合。
        	if(result == null)
        		errorMessage();
        	else
        		db = result;
        }
    }
    
	//DBが読み込み出来なかった場合にTOASTでメッセージを表示する．
    private void errorMessage(){
    	Toast.makeText(context, 
    			"ストレージにエラーがあるか容量が足りないためデータベースを読み込めませんでした。ファイルを消去して再度起動してください。",
    			Toast.LENGTH_LONG).show();
    }
    
    
	/**
	 *データベースの読み込み（SDカード）
	 *
	 */
    private SQLiteDatabase getDatabase_SD(){
    	try{
			android.util.Log.v("OpenDBHelper","SDCard");
			dbHelper = new DatabaseHelper(this.context,DB_NAME);
			return dbHelper.getWritableDatabase();
		}catch(SQLiteException e){
			return null;
		}
    }
    
	/**
	 *データベースの読み込み（内部ストレージ）
	 *
	 */
    private SQLiteDatabase getDatabase_Storage(){
    	try{
    		android.util.Log.v("OpenDBHelper","Storage");
    		dbHelper = new DatabaseHelper(this.context,INDB_NAME);
    		return dbHelper.getWritableDatabase();
		}catch(SQLiteException e){
			return null;
		}
    }
    
	
	/**
	 *外部からデータベースにアクセスするインスタンスを取得する
	 *
	 */
    public static synchronized ImageDatabaseAdapter getSingleton(Context context){
    	if(imageAdapter == null)
    		return imageAdapter = new ImageDatabaseAdapter(context);
    	else
    		return imageAdapter;
    }
    
	
	/**
	 *データベースをクローズする
	 *
	 */
    public void Close(){
    	try {
    		if(imageAdapter != null){
    	    	db.close();
    	    	dbHelper.close();
    	    	Log.v("IconDataBase","Closed!");
    		}
		} catch (Throwable e) {
			e.printStackTrace();
		}
    	imageAdapter = null;
    }
    
    //
    // SQLiteOpenHelper
    //


	
	/**
	 *データベースの読み書きをサポートするヘルパークラス
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

    	/** データベースバージョン */
        private static final int DB_VERSION = 1;
    	
    	public DatabaseHelper(Context context,String db_name) {
    			super(context, db_name, null, DB_VERSION);
    	}

    	@Override
    	public void onCreate(SQLiteDatabase db) {
    		db.execSQL(CREATE_TABLE);
    	}

    	@Override
    	public void onUpgrade(
    			SQLiteDatabase db,
    			int oldVersion,
    			int newVersion) {
    		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    		onCreate(db);
    	}

    }
    
	
	/**
	 *SDカードの読み書きをチェックする
	 *
	 */
	public boolean isSdCanWrite() {
		//SDカードがあるかチェック
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}

		File file = Environment.getExternalStorageDirectory();
		if (file.canWrite()){
			return true;
		}
		return false;
	}
    
 
    /*
    public void close(){
      dbHelper.close();
    }*/
     
     
    /**
     * SD上のデータベースに行を登録する。
     * 既に同じ行があった場合は行を削除の後投入する。
     * 
     * @params content1 検索用の何か
     * @params content2 内容
     * @throws Exception トランザクションエラー時（本人も良く分かってない）
     */
    public synchronized void insert(String url, Bitmap picture) {
    	
    	if(db == null)return;
    	try {
    		db.beginTransaction();
    		// 行削除用の検索文作成
    		
    			String whereClause =
    					new StringBuilder()
    			.append("Url")
    			.append(" == ")
    			.append(url)
    			.toString();
    		 


    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		picture.compress(CompressFormat.PNG, 100, baos);
    		byte[] bitmap = baos.toByteArray();
    		baos.close();

    		// 行挿入用ContentValuesの作成
    		ContentValues values = new ContentValues();
    		values.put("Url", url);
    		values.put("Date", System.currentTimeMillis());
    		values.put("Icon", bitmap);

    		// 既に行があった場合は削除
    		try{
    			db.delete("IconList", whereClause, null);
    		}catch(Exception e){
    			//e.printStackTrace();
    		}
    		// 行の新規挿入
    		db.insert("IconList", null, values);
    		
    		/*
        	db.rawQuery("insert into IconList( Url, Date, Icon)values('" + url + "', " 
        	+ System.currentTimeMillis() +" ,"+ bitmap +");", null).close();
        	*/
    		 

    		db.setTransactionSuccessful();
        	
        	android.util.Log.v("NewIcon", url);

    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
			//db.close();
			//dbHelper.close();
    		db.endTransaction();
    		url = null;
    	}

    }
    
    /**
     * SD上のデータベースに複数行を登録する。
     * 既に同じ行があった場合は行を削除の後投入する。
     * 
     * @params content1 検索用の何か
     * @params content2 内容
     * @throws Exception トランザクションエラー時（本人も良く分かってない）
     */
    public Bitmap getData(String url){
    	Bitmap result = null;

    	if(db == null)return result;
    	//db = dbHelper.getReadableDatabase();
    	try {
        	db.beginTransaction();
    		long dTime = System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 5;

    		// 行の新規挿入
    		Cursor c = db.rawQuery("select * from IconList where Url = '" + url + "';", null);
    		byte[] pic = null;
    		long date = -1;
    		try{
    			if(c.moveToFirst()){
    				//android.util.Log.v("DBPictureGet!", "hit");
    				date = c.getLong(c.getColumnIndex("Date"));
    				pic = c.getBlob(c.getColumnIndex("Icon"));
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{	
    			if(c != null && !c.isClosed()){
    				c.close();
    			} 
    		}

    		try{
    			result = BitmapFactory.decodeByteArray(pic, 0, pic.length);
    			//android.util.Log.v("DBPictureGet!", "picture");
    		}catch(Exception e){
    			//android.util.Log.v("DBPictureUnHit!", "unHit");
    			return null;
    		}

    		if(dTime > date){
    			// 既に行があった場合は削除
    			db.rawQuery("delete from IconList where Url = '" + url + "';", null);
    		}

    		db.setTransactionSuccessful();

    	}catch (Exception e) {
    		result = null;
    		//e.printStackTrace();
    	} finally {
    		db.endTransaction();
    		//db.close();
    		//dbHelper.close();
    		url = null;
    	}

    	return result;

    }
    
    /**
     * SD上のデータベースに行を消去する。
     * 日付の古い物を消去
     * 
     * @params content1 検索用の何か
     * @params content2 内容
     * @throws Exception トランザクションエラー時（本人も良く分かってない）
     */
    public void oldDelete(Context context,SQLiteDatabase db) {

    	long dTime = System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 5;
    	if(db == null) return;
    	 //db.beginTransaction();
    	try{
    		//db = dbHelper.getWritableDatabase();
    		try {
    			db.beginTransaction();

    			// 行削除用の検索文作成
    			String whereClause =
    					new StringBuilder()
    			.append("Date")
    			.append(" <= ")
    			.append(dTime)
    			.toString();


    			// 既に行があった場合は削除
    			int num = db.delete("IconList", whereClause, null);
    			android.util.Log.v("DeleteIcon","num:" + num);

    			db.setTransactionSuccessful();

    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			db.endTransaction();
    			//db.close();
    			//dbHelper.close();
    		}
    	}catch(Exception e){}

    }
    
    
}