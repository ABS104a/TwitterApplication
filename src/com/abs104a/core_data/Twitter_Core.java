package com.abs104a.core_data;

import android.content.Context;

public class Twitter_Core {

	//�ۑ�����Ă���Twitter�N���X�̃��X�g
	private final TwitterList twitterList;
	
	//���X�g�̎擾
	public final TwitterList getTwitterList() {
		return twitterList;
	}

	/**
	 * �R���X�g���N�^
	 * Twitter�̃����o�[���X�g��ǂݍ���
	 * @param con �A�v���P�[�V�����̃R���e�L�X�g
	 */
	public Twitter_Core(Context con){
		//Twitter�̃����o�[���X�g�쐬
		twitterList = new TwitterList(con);
	}
}
