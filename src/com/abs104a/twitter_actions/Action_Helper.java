package com.abs104a.twitter_actions;

import com.abs104a.core_data.TwitterStatus;

import android.content.Context;
import android.widget.ArrayAdapter;
import twitter4j.Twitter;

/**
 * Twitterを使ってアクションをする時に使用するデータヘルパー
 * @author Kouki
 *
 */
public class Action_Helper {

	private final ArrayAdapter<?> adapter;
	private final Context con;
	
	public final ArrayAdapter<?> getAdapter() {
		return adapter;
	}

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
	private final TwitterStatus[] status;
	
	/**
	 * Actionを行う対象のTweetを返す
	 * @return 対象のTwitterクラス
	 */
	public final TwitterStatus[] getStatus() {
		return status;
	}

	/**
	 * Action_Helperを設定
	 * @param twitter 動作を行うTwitterクラス
	 * @param status 動作を行うTweet
	 */
	public Action_Helper(
			Context con,
			ArrayAdapter<?> adapter,
			Twitter twitter,
			TwitterStatus status){
		this.con = con;
		this.adapter = adapter;
		this.twitter = twitter;
		this.status = new TwitterStatus[1];
		this.status[0] = status;
	}
	
	/**
	 * Action_Helperを設定
	 * @param twitter 動作を行うTwitterクラス
	 * @param status 動作を行うTweet
	 */
	public Action_Helper(
			Context con,
			ArrayAdapter<?> adapter,
			Twitter twitter,
			TwitterStatus[] status){
		this.con = con;
		this.adapter = adapter;
		this.twitter = twitter;
		this.status = status;
	}
}
