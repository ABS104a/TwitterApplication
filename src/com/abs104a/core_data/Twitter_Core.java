package com.abs104a.core_data;

import android.content.Context;

public class Twitter_Core {

	//保存されているTwitterクラスのリスト
	private final TwitterList twitterList;
	
	//リストの取得
	public final TwitterList getTwitterList() {
		return twitterList;
	}

	/**
	 * コンストラクタ
	 * Twitterのメンバーリストを読み込む
	 * @param con アプリケーションのコンテキスト
	 */
	public Twitter_Core(Context con){
		//Twitterのメンバーリスト作成
		twitterList = new TwitterList(con);
	}
}
