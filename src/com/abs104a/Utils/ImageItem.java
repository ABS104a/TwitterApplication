package com.abs104a.Utils;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * ImageItem
 * @author Kouki
 *イメージをダウンロードする際threadにあふれたItemをキューするためのクラス
 */
public class ImageItem{
	public ImageView imageView = null;
	public TextView nameView = null;
	public String name = null;
	public String url = null;
	public boolean isSave = true;
	public boolean isRetweet = false;
	public boolean isThumb = false;
}