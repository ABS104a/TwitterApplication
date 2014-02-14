package com.abs104a.twitter_actions;

import com.abs104a.core_data.TwitterStatus;

import twitter4j.TwitterException;
import android.os.AsyncTask;



public final class Favorite {
	
	/**
	 * ���C�ɓ���ɒǉ����郁�\�b�h
	 * @param helper ����w���p�[
	 */
	public void createFavorite(Action_Helper helper){
		new FavoriteTask(helper).execute();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * �f�[�^��r���X�V����]���p�N���X
	 * @author Kouki
	 *
	 */
	class UpdateData {
		final private TwitterStatus status;
		public final TwitterStatus getStatus() {
			return status;
		}

		public final twitter4j.Status getResultStatus() {
			return resultStatus;
		}

		final private twitter4j.Status resultStatus;
		
		public UpdateData(TwitterStatus status,twitter4j.Status resultStatus){
			this.status  = status;
			this.resultStatus = resultStatus;
		}
	}
	
	/**
	 * ���C�ɓ���ɒǉ�����N���X
	 * @author Kouki
	 *
	 */
	final class FavoriteTask extends AsyncTask<Void, UpdateData, Integer>{

		
		private final Action_Helper helper;

		public FavoriteTask(Action_Helper helper){
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
			int counter = 0;
			for(TwitterStatus status : helper.getStatus()){
				try{
					final twitter4j.Status resultStatus;
					if(status.getStatus().isFavorited()){
						//���C�ɓ���ɒǉ�����Ă��鎞
						resultStatus = helper.getTwitter()
								.createFavorite(status.getStatus().getId());
					}else{
						//���C�ɓ���ɒǉ�����Ă��Ȃ���
						resultStatus = helper.getTwitter()
								.destroyFavorite(status.getStatus().getId());
					}
					publishProgress(new UpdateData(status, resultStatus));
					counter++;
				}catch(TwitterException e){
					e.printStackTrace();
				}
			}
			return counter;
		}
		
		/**
		 * �ӂ��ڐ���������
		 */
		@Override
		protected void onProgressUpdate(UpdateData... values) {
			if(values != null && values.length > 0){
				values[0].status.setStatus(values[0].resultStatus);
				helper.getAdapter().notifyDataSetChanged();
			}
		}
	}
}
