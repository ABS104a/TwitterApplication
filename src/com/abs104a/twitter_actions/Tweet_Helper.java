package com.abs104a.twitter_actions;

import android.content.Context;
import twitter4j.Twitter;

/**
 * Twitterを使ってアクションをする時に使用するデータヘルパー
 * @author Kouki
 *
 */
public class Tweet_Helper {

	private final Context con;

	public final Context getCon() {
		return con;
	}

	//アクションを行うTwitter
	private final Twitter twitter;
	
	/**
	 * Actionを行う対象のTwitterを返す
	 * @return 対象のTwitterクラス
	 */
	public final Twitter getTwitter() {
		return twitter;
	}

	//アクションを行うTweet
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
	 * Tweetをするためのヘルパークラス
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
