package com.abs104a.twitter_actions;

import android.content.Context;
import twitter4j.Twitter;

/**
 * Twitter���g���ăA�N�V���������鎞�Ɏg�p����f�[�^�w���p�[
 * @author Kouki
 *
 */
public class Tweet_Helper {

	private final Context con;

	public final Context getCon() {
		return con;
	}

	//�A�N�V�������s��Twitter
	private final Twitter twitter;
	
	/**
	 * Action���s���Ώۂ�Twitter��Ԃ�
	 * @return �Ώۂ�Twitter�N���X
	 */
	public final Twitter getTwitter() {
		return twitter;
	}

	//�A�N�V�������s��Tweet
	private final String tweet;
	public final String getTweet() {
		return tweet;
	}

	public final long getReplyStatusId() {
		return replyStatusId;
	}

	public final String[] getImageUri() {
		return imageUri;
	}

	private final long replyStatusId;
	private final String[] imageUri;

	/**
	 * Tweet�����邽�߂̃w���p�[�N���X
	 * @param con
	 * @param twitter
	 * @param tweet
	 * @param replyId
	 * @param imageUri
	 */
	public Tweet_Helper(Context con,
			Twitter twitter,
			String tweet,
			long replyStatusId,
			String[] imageUri){
		this.con = con;
		this.twitter = twitter;
		this.tweet = tweet;
		this.replyStatusId = replyStatusId;
		this.imageUri = imageUri;
	}
}
