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
 *���[�U�[�A�C�R�����Ǘ����邽�߂̃f�[�^�[�x�[�X�A�_�v�^�[
 *�ǂݍ��݂Ə������݂��s���D
 */
public final class ImageDatabaseAdapter  {
    

	/** �f�[�^�x�[�X��u��SD��̃t�H���_�̃t���p�X */
    private static final String DB_DIRECTORY =
        Environment.getExternalStorageDirectory() + "/TwitterLook/";
    
    private static final String INDB_NAME = 
    		"user_icon.db";
     
    /** �f�[�^�x�[�X�t�@�C���̃t���p�X */
    private static final String DB_NAME =
        DB_DIRECTORY + INDB_NAME;
    
     
     
    /** �e�[�u���쐬��SQL�� */
    private static final String CREATE_TABLE =
        "create table if not exists IconList (" +
        "_id integer primary key autoincrement, " +
        "Url text, " +
        "Date integer, " +
        "Icon blob" +
        ");";
     
	//�f�[�^�x�[�X
    private SQLiteDatabase db = null;

	//�A�v���̃R���e�L�X�g
	private final Context context;

	//�f�[�^�x�[�X�w���p�[�i�I�[�v���p
	private DatabaseHelper dbHelper = null;
	
	//Singleton�p
	private static ImageDatabaseAdapter imageAdapter = null;
    
	//�R���X�g���N�^
    private ImageDatabaseAdapter(Context context){
    	this.context = context;
    	//DB��񓯊��œǂ݂ɍs��
    	readDB();
    	
    }
    
    private void readDB(){
    	new OpenDataBaseTask().execute();
    }
    
    private class OpenDataBaseTask extends AsyncTask<Void, Void, SQLiteDatabase> {
        protected SQLiteDatabase doInBackground(Void... args) {
        	//SD�J�[�h��D��ŏ������݉\���m�F�C�ǂݍ��ݏo���Ȃ��ꍇ�͓�����������
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
        	//DB�̓ǂݍ��݂Ɏ��s�����ꍇ�B
        	if(result == null)
        		errorMessage();
        	else
        		db = result;
        }
    }
    
	//DB���ǂݍ��ݏo���Ȃ������ꍇ��TOAST�Ń��b�Z�[�W��\������D
    private void errorMessage(){
    	Toast.makeText(context, 
    			"�X�g���[�W�ɃG���[�����邩�e�ʂ�����Ȃ����߃f�[�^�x�[�X��ǂݍ��߂܂���ł����B�t�@�C�����������čēx�N�����Ă��������B",
    			Toast.LENGTH_LONG).show();
    }
    
    
	/**
	 *�f�[�^�x�[�X�̓ǂݍ��݁iSD�J�[�h�j
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
	 *�f�[�^�x�[�X�̓ǂݍ��݁i�����X�g���[�W�j
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
	 *�O������f�[�^�x�[�X�ɃA�N�Z�X����C���X�^���X���擾����
	 *
	 */
    public static synchronized ImageDatabaseAdapter getSingleton(Context context){
    	if(imageAdapter == null)
    		return imageAdapter = new ImageDatabaseAdapter(context);
    	else
    		return imageAdapter;
    }
    
	
	/**
	 *�f�[�^�x�[�X���N���[�Y����
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
	 *�f�[�^�x�[�X�̓ǂݏ������T�|�[�g����w���p�[�N���X
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

    	/** �f�[�^�x�[�X�o�[�W���� */
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
	 *SD�J�[�h�̓ǂݏ������`�F�b�N����
	 *
	 */
	public boolean isSdCanWrite() {
		//SD�J�[�h�����邩�`�F�b�N
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
     * SD��̃f�[�^�x�[�X�ɍs��o�^����B
     * ���ɓ����s���������ꍇ�͍s���폜�̌㓊������B
     * 
     * @params content1 �����p�̉���
     * @params content2 ���e
     * @throws Exception �g�����U�N�V�����G���[���i�{�l���ǂ��������ĂȂ��j
     */
    public synchronized void insert(String url, Bitmap picture) {
    	
    	if(db == null)return;
    	try {
    		db.beginTransaction();
    		// �s�폜�p�̌������쐬
    		
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

    		// �s�}���pContentValues�̍쐬
    		ContentValues values = new ContentValues();
    		values.put("Url", url);
    		values.put("Date", System.currentTimeMillis());
    		values.put("Icon", bitmap);

    		// ���ɍs���������ꍇ�͍폜
    		try{
    			db.delete("IconList", whereClause, null);
    		}catch(Exception e){
    			//e.printStackTrace();
    		}
    		// �s�̐V�K�}��
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
     * SD��̃f�[�^�x�[�X�ɕ����s��o�^����B
     * ���ɓ����s���������ꍇ�͍s���폜�̌㓊������B
     * 
     * @params content1 �����p�̉���
     * @params content2 ���e
     * @throws Exception �g�����U�N�V�����G���[���i�{�l���ǂ��������ĂȂ��j
     */
    public Bitmap getData(String url){
    	Bitmap result = null;

    	if(db == null)return result;
    	//db = dbHelper.getReadableDatabase();
    	try {
        	db.beginTransaction();
    		long dTime = System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 5;

    		// �s�̐V�K�}��
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
    			// ���ɍs���������ꍇ�͍폜
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
     * SD��̃f�[�^�x�[�X�ɍs����������B
     * ���t�̌Â���������
     * 
     * @params content1 �����p�̉���
     * @params content2 ���e
     * @throws Exception �g�����U�N�V�����G���[���i�{�l���ǂ��������ĂȂ��j
     */
    public void oldDelete(Context context,SQLiteDatabase db) {

    	long dTime = System.currentTimeMillis() - 60 * 60 * 1000 * 24 * 5;
    	if(db == null) return;
    	 //db.beginTransaction();
    	try{
    		//db = dbHelper.getWritableDatabase();
    		try {
    			db.beginTransaction();

    			// �s�폜�p�̌������쐬
    			String whereClause =
    					new StringBuilder()
    			.append("Date")
    			.append(" <= ")
    			.append(dTime)
    			.toString();


    			// ���ɍs���������ꍇ�͍폜
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