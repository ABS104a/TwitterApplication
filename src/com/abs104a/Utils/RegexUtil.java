package com.abs104a.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

//文字を抜き出すクラス
//Via表示から文字列だけを抽出する
public class RegexUtil {

	private static final String TWITPIC = "http://twitpic[.]com/([a-zA-Z_0-9]+)";
	private static final String YFLOG = "^http://(?:yfrog[.]com|twitter.yfrog[.]com)/([a-zA-Z_0-9]+)";
	private static final String TWEETGOO = "http://twitgoo[.]com/([a-zA-Z_0-9]+)";
	private static final String TWIPPLE = "http://p.twipple[.]jp/([a-zA-Z_0-9]+)";
	private static final String LOCKERS = "http://lockerz[.]com/s/([a-zA-Z_0-9]+)";
	private static final String IMGLY = "http://img[.]ly/([a-zA-Z_0-9]+)";
	private static final String PHOTOZOU = "http://photozou.jp/photo/show/[0-9]+/([0-9]+)";
	private static final String MOVAPIC  = "http://movapic.com/pic/([a-zA-Z_0-9]+)";
	private static final String USER  = "https://twitter.com/([a-zA-Z_0-9]+)";
	private static final String NIKO = "^http://(?:(?:www|ext).nicovideo.jp/watch|nico.ms)/(?:sm|nm)?([0-9]+)(?:.+)?$";
	private static final String YOUTUBE = "^http://(?:.+?.youtube.com|youtu.be)/(?:watch\\?v=)?([a-zA-Z_0-9]+)";
	private static final String Tumblr = "http://.+?.tumblr.com/post/([0-9]+)(/(.+?)/)";
	private static final String Instagram = "(http://(?:instagr.am)|(?:instagram.com)/p/[a-zA-Z_0-9]+)";

	private static final String TWEETSTATUS = "https://twitter.com/[a-zA-Z_0-9]+/status/([0-9]+)";

	 /**
	  * pxからdipへの変換処理
	  */
	 public static float px2Dip(Activity activity, float pixel) {
	     float dip = 0;
	     DisplayMetrics metrics = new DisplayMetrics();
	     activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	     dip = pixel *  metrics.scaledDensity ;
	     return dip;
	 }
	 
	 @SuppressLint("NewApi")
	public static long getUsableMem(File item) {  
   	  
	        long size = 0; 
	        if(Build.VERSION.SDK_INT >= 18){
	        	try {  
	        		StatFs fs = new StatFs(item.getAbsolutePath());  
	        		size = fs.getBlockSizeLong() * fs.getAvailableBlocksLong();  
	        		size = size / (1024 * 1024);
	        	} catch (IllegalArgumentException e) {  
	        		e.printStackTrace();
	        	}  
	        }else{
	        	try {  
	        		StatFs fs = new StatFs(item.getAbsolutePath());  
	        		size = (long)fs.getBlockSize() * (long)fs.getAvailableBlocks();  
	        		size = size / (1024 * 1024);
	        	} catch (IllegalArgumentException e) {  
	        		e.printStackTrace();
	        	}  
	        }
	        return size;  
	    }
	    
	 public static long getInternalUseableMem() {  
		 return getUsableMem(Environment.getDataDirectory());  
	 }  
	 
	 public static long getExternalUseableMem() {  
		 return getUsableMem(Environment.getExternalStorageDirectory());  
	 }  

	public static String extractMatchString( String target) {
		String regex = "\\>(.+)\\<";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "web";
		}
	}
	
	  // 標準的なURL抽出パターン
	  public static final Pattern STANDARD_URL_MATCH_PATTERN = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE);
	private static final String SAVERTAG = "TwitterBiyonRtagMode";

	  // 貪欲なURL抽出パターン
	  //public static final Pattern GREEDY_URL_MATCH_PATTERN = Pattern.compile("(http|https):([^\\x00-\\x20()\"<>\\x7F-\\xFF])*", Pattern.CASE_INSENSITIVE);


	  //文字列からURLを抽出
	  public static String[] extract(Pattern pattern, String text){
	    List<String> list = new ArrayList<String>();
	    Matcher matcher = pattern.matcher(text);
	    while(matcher.find()){
	      list.add(matcher.group());
	    }
	    return list.toArray(new String[list.size()]);
	  }

	  //文字列からURLを抽出
	  public static SearchStringsMatch[] extractSearch(String regex, String text){
		Pattern pattern = Pattern.compile(regex);
	    List<SearchStringsMatch> list = new ArrayList<SearchStringsMatch>();
	    Matcher matcher = pattern.matcher(text);
	    while(matcher.find()){
	      list.add(new SearchStringsMatch(matcher.start(),matcher.end()));
	    }
	    return list.toArray(new SearchStringsMatch[list.size()]);
	  }

	public static String extractMatchString(String regex, String target)throws IllegalStateException {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			throw new IllegalStateException("No match found.");
		}
	}

	public static Long statusUrlExtractMatchString(final String target){
		String result = null;

		try{
			result = extractMatchString(TWEETSTATUS,target);
			return Long.parseLong(result);
		}catch(IllegalStateException e){

		}
		return null;
	}

	public static String userUrlExtractMatchString(final String target){
		String result = null;

		try{
			result = extractMatchString(USER,target);
			return result;
		}catch(IllegalStateException e){

		}
		return null;
	}

	public static String imageUrlExtractMatchString(final String target,final boolean isThumb){
		String result = null;

		try{
			result = extractMatchString(TWITPIC,target);
			if(isThumb)
				return String.format("http://twitpic.com/show/thumb/%s" ,result);
			else
				return String.format("http://twitpic.com/show/full/%s" ,result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(YFLOG,target);
			if(isThumb)
				return String.format("http://yfrog.com/%s:small",result);
			else
				return String.format("http://yfrog.com/%s:medium", result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(TWEETGOO,target);
			if(isThumb)
				return String.format("http://twitgoo.com/show/thumb/%s",result);
			else
				return String.format("http://twitgoo.com/show/img/%s",result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(TWIPPLE,target);
			if(isThumb)
				return String.format("http://p.twpl.jp/show/thumb/%s",result);
			else
				return String.format("http://p.twpl.jp/show/large/%s",result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(LOCKERS,target);
			if(isThumb)
				return String.format("http://api.plixi.com/api/tpapi.svc/imagefromurl?url=http://lockerz.com/s/%s?size=small",result);
			else
				return String.format("http://api.plixi.com/api/tpapi.svc/imagefromurl?url=http://lockerz.com/s/%s?size=big",result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(IMGLY,target);
			if(isThumb)
				return String.format("http://img.ly/show/thumb/%s",result);
			else
				return String.format("http://img.ly/show/full/%s",result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(PHOTOZOU,target);
			if(isThumb)
				return String.format("http://photozou.jp/p/thumb/%s",result);
			else
				return String.format("http://photozou.jp/bin/photo/%s/org.bin",result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(MOVAPIC,target);
			if(isThumb)
				return String.format("http://image.movapic.com/pic/t_%s.jpeg",result);
			else
				return String.format("http://image.movapic.com/pic/m_%s.jpeg",result);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(Tumblr,target);
			if(isThumb)
				return String.format("http://img.azyobuzi.net/api/redirect?uri=%s&size=thumb",target);
			else
				return String.format("http://img.azyobuzi.net/api/redirect?uri=%s",target);
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(NIKO,target);
			if(isThumb)
				return String.format("http://tn-skr3.smilevideo.jp/smile?i=%s",result);
			else
				return null;
		}catch(IllegalStateException e){
			//Log.v("NIKO", "ERROR");
		}

		try{
			result = extractMatchString(YOUTUBE,target);
			if(isThumb)
				return String.format("http://img.youtube.com/vi/%s/mqdefault.jpg",result);
			else
				return null;
		}catch(IllegalStateException e){

		}

		try{
			result = extractMatchString(Instagram,target);
			if(isThumb)
				return String.format("%smedia/?size=t",target);
			else
				return String.format("%smedia/?size=l",target);
		}catch(IllegalStateException e){

		}

		try{
			if(!isThumb){
				if(target.endsWith(".jpeg") || target.endsWith(".jpg"))
					return target;
			}
		}catch(Exception e){

		}

		return null;

	}

	public static void saveRtag(Context context,String text){
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		sp.edit().putString(SAVERTAG, text).commit();
	}

	public static String readRtag(Context context){
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(SAVERTAG, null);
	}

}
