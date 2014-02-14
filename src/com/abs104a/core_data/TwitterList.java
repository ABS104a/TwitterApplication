package com.abs104a.core_data;

import java.util.ArrayList;

import android.content.Context;

import twitter4j.Twitter;

/**
 * マルチアカウント用
 * Twitterアカウント管理リスト
 * @author Kouki
 *
 */
public final class TwitterList extends ArrayList<Twitter>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1582092467377649887L;
	
	//デフォルトの使用アカウント位置
	private int default_position = 0;

	//Applicationのコンテキスト
	private final Context context;

	/**
	 * デフォルトのアカウント位置を取得する
	 * @return デフォルトアカウント位置
	 */
	public final int getDefault_position() {
		return default_position;
	}


	/**
	 * デフォルトのアカウント位置を設定する
	 * @param default_position 設定するデフォルト位置
	 * @return 設定に成功したかどうか
	 */
	public final boolean setDefault_position(int default_position) {
		//デフォルトポジションが配列の範囲外の時
		if(default_position < 0 || default_position >= this.size()){
			return false;
		}else{
			this.default_position = default_position;
			return true;
		}
	}


	/**
	 * TwitterListコンストラクタ
	 * リストの読み込みを行う
	 * @param con
	 */
	public TwitterList(Context con){
		this.context = con;
		//TODO　ここでリストの読み込みをする　
	}

	
	/**
	 * リストの永続化処理を行う
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO 永続化処理を行う
		super.finalize();
	}
	
	

	
}
