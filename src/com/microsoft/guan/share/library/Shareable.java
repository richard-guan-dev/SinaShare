package com.microsoft.guan.share.library;

import android.app.Activity;

public interface Shareable {
	public void shareText(String text, ShareCallBack callback);

	public void shareTextWithImage(String text, String imageUrl,
			ShareCallBack callback);

	public void init(Activity context);

	public void getAuth();

	public void releastAuth();
}
