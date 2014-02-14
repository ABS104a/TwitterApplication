package com.abs104a.core_data;

import java.util.ArrayList;

import android.content.Context;

import twitter4j.Twitter;

/**
 * �}���`�A�J�E���g�p
 * Twitter�A�J�E���g�Ǘ����X�g
 * @author Kouki
 *
 */
public final class TwitterList extends ArrayList<Twitter>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1582092467377649887L;
	
	//�f�t�H���g�̎g�p�A�J�E���g�ʒu
	private int default_position = 0;

	//Application�̃R���e�L�X�g
	private final Context context;

	/**
	 * �f�t�H���g�̃A�J�E���g�ʒu���擾����
	 * @return �f�t�H���g�A�J�E���g�ʒu
	 */
	public final int getDefault_position() {
		return default_position;
	}


	/**
	 * �f�t�H���g�̃A�J�E���g�ʒu��ݒ肷��
	 * @param default_position �ݒ肷��f�t�H���g�ʒu
	 * @return �ݒ�ɐ����������ǂ���
	 */
	public final boolean setDefault_position(int default_position) {
		//�f�t�H���g�|�W�V�������z��͈̔͊O�̎�
		if(default_position < 0 || default_position >= this.size()){
			return false;
		}else{
			this.default_position = default_position;
			return true;
		}
	}


	/**
	 * TwitterList�R���X�g���N�^
	 * ���X�g�̓ǂݍ��݂��s��
	 * @param con
	 */
	public TwitterList(Context con){
		this.context = con;
		//TODO�@�����Ń��X�g�̓ǂݍ��݂�����@
	}

	
	/**
	 * ���X�g�̉i�����������s��
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO �i�����������s��
		super.finalize();
	}
	
	

	
}
