package com.abs104a.twitter_actions;

import com.abs104a.core_data.TwitterStatus;

import android.content.Context;
import android.widget.ArrayAdapter;
import twitter4j.Twitter;

/**
 * Twitter���g���ăA�N�V���������鎞�Ɏg�p����f�[�^�w���p�[
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
	private final TwitterStatus[] status;
	
	/**
	 * Action���s���Ώۂ�Tweet��Ԃ�
	 * @return �Ώۂ�Twitter�N���X
	 */
	public final TwitterStatus[] getStatus() {
		return status;
	}

	/**
	 * Action_Helper��ݒ�
	 * @param twitter ������s��Twitter�N���X
	 * @param status ������s��Tweet
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
	 * Action_Helper��ݒ�
	 * @param twitter ������s��Twitter�N���X
	 * @param status ������s��Tweet
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
