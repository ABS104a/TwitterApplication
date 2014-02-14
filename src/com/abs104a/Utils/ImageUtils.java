package com.abs104a.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;


/*
 * �摜�̃��T�C�Y���s���֐�
 * Twitter�A�C�R���̉摜�����T�C�Y����
 */
public class ImageUtils {

    private static final String TAG = "ImageUtils";

    public static Bitmap resizeBitmapToDisplaySize(Bitmap src,float size){
        int srcWidth = src.getWidth(); // ���摜��width
        int srcHeight = src.getHeight(); // ���摜��height

        // ��ʃT�C�Y���擾����
        Matrix matrix = new Matrix();
        float screenWidth = size;
        float screenHeight = size;

        float widthScale = screenWidth / srcWidth;
        float heightScale = screenHeight / srcHeight;
        Log.d(TAG, "widthScale = " + String.valueOf(widthScale)
                + ", heightScale = " + String.valueOf(heightScale));
        if (widthScale > heightScale) {
            matrix.postScale(heightScale, heightScale);
        } else {
            matrix.postScale(widthScale, widthScale);
        }
        // ���T�C�Y
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
        //if(!dst.equals(src))src.recycle();
        src = null;
        return dst;
    }
    
    public static Bitmap getAppBackGroundData(Activity activity){
    	InputStream input = null;
    	try {
    	    input = activity.openFileInput("background_image.png");
    	} catch (FileNotFoundException e) {
    	    // �G���[����
    		e.printStackTrace();
    		return null;
    	}   
    	return BitmapFactory.decodeStream(input);
    }
    
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {  
    	  
        // inJustDecodeBounds=true �ŉ摜�̃T�C�Y���`�F�b�N  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(filePath, options);  
      
        // inSampleSize ���v�Z  
        options.inSampleSize = calculateInImageSize(options, reqWidth, reqHeight);  
      
        // inSampleSize ���Z�b�g���ăf�R�[�h  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeFile(filePath, options);  
    }  
    
    public static int calculateInImageSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {  
    	  
        // �摜�̌��T�C�Y  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
      
        if (height > reqHeight || width > reqWidth) {  
            if (width > height) {  
                inSampleSize = Math.round((float)height / (float)reqHeight);  
            } else {  
                inSampleSize = Math.round((float)width / (float)reqWidth);  
            }  
        }  
        return inSampleSize;  
    } 
    
    
    
    public static Bitmap resizeBitmapToDisplaySize(Activity activity, Bitmap src){
        int srcWidth = src.getWidth(); // ���摜��width
        int srcHeight = src.getHeight(); // ���摜��height
 
        // ��ʃT�C�Y���擾����
        Matrix matrix = new Matrix();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenWidth = (float) metrics.widthPixels;
        float screenHeight = (float) metrics.heightPixels;
 
        float widthScale = screenWidth / srcWidth;
        float heightScale = screenHeight / srcHeight;
        Log.d(TAG, "widthScale = " + String.valueOf(widthScale)
                + ", heightScale = " + String.valueOf(heightScale));
        if (widthScale < heightScale) {
            matrix.postScale(heightScale, heightScale);
        } else {
            matrix.postScale(widthScale, widthScale);
        }
        // ���T�C�Y
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
        if(!dst.equals(src))src.recycle();
        src = null;
        return dst;
    }
    
    /**
     * �摜���p�ۂɂ���
     * @param bm
     * @return
     */
    public static Bitmap RadiusImage(Bitmap bm){
        int width  = bm.getWidth();
        int height = bm.getHeight();
        int size = Math.min(width, height);
        Bitmap clipArea = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(clipArea);
        c.drawRoundRect(new RectF(0, 0, size, size), size/10, size/10, new Paint(Paint.ANTI_ALIAS_FLAG));
        Bitmap newImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newImage);
        Paint paint = new Paint();
        canvas.drawBitmap(clipArea, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bm, new Rect(0, 0, size, size), new Rect(0, 0, size, size), paint);
        bm.recycle();
        return newImage;
    }


    public void saveImage(Context context,Bitmap bmp){
    	new SaveImage(context,bmp).execute();
    }
    /**
     * �摜��ۑ�����N���X
     * @param context
     * @param bmp
     */
	public class SaveImage extends AsyncTask<Void,Void,Boolean>{

		private Context context;
		private Bitmap bmp;
		private File path;
		private ProgressDialog waitDialog;

		SaveImage(Context context,Bitmap bmp){
			this.context = context;
			this.bmp = bmp;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(waitDialog != null)
				waitDialog.dismiss();
			waitDialog = null;// �_�C�A���O�̏���
			if(result)
				Toast.makeText(context, "Success!��(�@��:)", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(context, "Failure��(�@��X)", Toast.LENGTH_SHORT).show();
			try{
				bmp.recycle();
				System.gc();
			}catch(Exception e){
				
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO �����������ꂽ���\�b�h�E�X�^�u
			super.onPreExecute();
			if(waitDialog == null){
				// �v���O���X�_�C�A���O�̐ݒ�
				waitDialog = new ProgressDialog(context);
				// �v���O���X�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
				waitDialog.setMessage("Saving...");
				waitDialog.setCanceledOnTouchOutside(false);
				// �~�X�^�C���i���邭����^�C�v�j�ɐݒ肵�܂�
				waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// �v���O���X�_�C�A���O��\��
				waitDialog.show();
			}
			File dir;
			if (ImageUtils.this.readySdcard()) { // 1
				dir = new File(ImageUtils.this.getDirPath());
				if (!dir.exists()) { // 2
					if (!dir.mkdirs())
						return;
				}
			} else {
				dir = context.getFilesDir(); // 3
			}

			path = new File(ImageUtils.this.getFilePath(dir)); // 4
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				saveImage(path, bmp);
				try {
					addGarally(context,path);
					return true;
				} catch (Exception e) {
				}
			} catch (Exception e) {
			}
			return false;
		}

	}

	private boolean readySdcard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	private String getDirPath() {
		return Environment.getExternalStorageDirectory().getPath() + "/MyApp/";
	}

	private String getFilePath(File dir) {
		Date d = new Date();

		String path = dir.getAbsolutePath() + "/";
		path += String.format("%4d%02d%02d%02d%02d%02d.jpg",
				(1900 + d.getYear()), (d.getMonth() + 1), d.getDate(),
				d.getHours(), d.getMinutes(), d.getSeconds());

		return path;
	}

	private void saveImage(File path, Bitmap bmp) throws Exception {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(path);
			bmp.compress(CompressFormat.JPEG, 100, out);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void addGarally(Context context,File path) throws Exception {
		try {
			ContentValues values = new ContentValues();
			ContentResolver contentResolver = context.getContentResolver();
			values.put(Images.Media.MIME_TYPE, "image/jpeg");
			values.put(Images.Media.DATE_MODIFIED,
					System.currentTimeMillis() / 1000);
			values.put(Images.Media.SIZE, path.length());
			values.put(Images.Media.TITLE, path.getName());
			values.put(Images.Media.DATA, path.getPath());
			contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values);
		} catch (Exception e) {
			throw e;
		}
	}
}