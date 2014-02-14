package com.abs104a.twitter_actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import com.abs104a.Utils.ImageUtils;
import com.abs104a.twitter_actions.Retweet.UpdateData;

public class Tweet {

	/**
	 * Tweet���s�����\�b�h
	 * @param helper
	 */
	public Tweet(Tweet_Helper helper){
		new TweetTask(helper).execute();
	}
	
	/**
	 * Tweet���s�����\�b�h
	 * @param con �A�v���P�[�V�����̃R���e�L�X�g
	 * @param twitter Tweet����Twitter
	 * @param tweet Tweet���e
	 */
	public Tweet(Context con,Twitter twitter,String tweet){
		new TweetTask(new Tweet_Helper(con, twitter, tweet, -1, null))
		.execute();
	}
	
	/**
	 * tweet����N���X
	 * @author Kouki
	 *
	 */
	final class TweetTask extends AsyncTask<Void, UpdateData, Integer>{

		
		private static final int MAX_IMAGE_SIZE = 1000;
		private final Tweet_Helper helper;
		private ArrayList<String> img = new ArrayList<String>();//�摜�Y�t�p

		public TweetTask(Tweet_Helper helper){
			this.helper = helper;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * �^�X�N���I��������
		 */
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			//result:������
			
		}

		@Override
		protected Integer doInBackground(Void... params) {

			try{
				//�摜������ꍇ
				String urlString = new String();
				if(helper.getImageUri() != null){
					//�摜���A�b�v���[�h����
					urlString = uploadImage();
				}
				StatusUpdate statusUpdate = new StatusUpdate(helper.getTweet() + urlString);
				statusUpdate.setInReplyToStatusId(helper.getReplyStatusId());
				helper.getTwitter().updateStatus(statusUpdate);
			}catch(TwitterException e){
				e.printStackTrace();
				return 0;
			}
			return 1;
		}
		
		/**
		 * �摜���A�b�v���[�h���郁�\�b�h
		 */
		private String uploadImage(){
			ConfigurationBuilder confbuilder  = new ConfigurationBuilder();


			try {
				confbuilder = confbuilder.setOAuthAccessToken(helper.getTwitter().getOAuthAccessToken().getToken())
				.setOAuthAccessTokenSecret(helper.getTwitter().getOAuthAccessToken().getTokenSecret())
				.setOAuthConsumerKey(helper.getTwitter().getConfiguration().getOAuthConsumerKey())
				.setOAuthConsumerSecret(helper.getTwitter().getConfiguration().getOAuthConsumerSecret())
				.setIncludeEntitiesEnabled(true).setUser(helper.getTwitter().getScreenName()).setUseSSL(true);

				MediaProvider media = MediaProvider.TWIPPLE;//TODO�@�����ŃA�b�v���[�_�[��I�ׂ�悤�ɂ���D
				confbuilder = confbuilder.setMediaProvider(media.name());
				//if(media == MediaProvider.TWITPIC)
					//confbuilder = confbuilder.setMediaProviderAPIKey(Const.TwiPicApiKey);
			} catch (TwitterException e) {
				e.printStackTrace();
			}

			Configuration conf = confbuilder.build();

			OAuthAuthorization auth = new OAuthAuthorization(conf);

			ImageUpload imageUpload = new ImageUploadFactory(conf).getInstance(auth);
			
			String str = new String();
			try {
				for(int i = img.size()-1;i >= 0;i--){
					final String pass = img.get(i);
					try{
						String name = new File(pass).getName();
						Bitmap picture;

						picture = ImageUtils.
								decodeSampledBitmapFromFile(pass, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
						
						if(picture == null)continue;
						ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
						picture.compress(CompressFormat.PNG, 80 , bos); 
						byte[] bitmapdata = bos.toByteArray();
						ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
						
						str += " " + imageUpload.upload(name,bs);

						try{
							bs.close();
							picture.recycle();
							picture = null;
						}catch(Exception e){
							e.printStackTrace();
						}
						System.gc();
						img.remove(i);
					}catch(Exception e){
						e.printStackTrace();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return str;
		}
		
	}
}
