package com.microsoft.guan.share.library;

import java.util.HashMap;

import android.app.Activity;

public class ShareBox {
	private static HashMap<String, Shareable> storage = new HashMap<String, Shareable>();
	private Activity context;
	private ShareCallBack listener;

	public ShareCallBack getListener() {
		return listener;
	}

	public void setListener(ShareCallBack listener) {
		this.listener = listener;
	}

	private static ShareBox shareBox;

	private ShareBox() {
	}

	public static ShareBox getInstance() {
		if (shareBox == null)
			shareBox = new ShareBox();
		return shareBox;
	}

	public void init(Activity context) {
		this.context = context;
	}

	public void shareText(String text, String method) {
		try {
			Shareable shareAble = storage.get(method);
			shareAble.init(context);
			shareAble.shareText(text, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void register(String method, Shareable share) {
		storage.put(method, share);
	}

	public void shareTextWithImage(String text, String imageUrl, String method) {
		try {
			Shareable shareAble = storage.get(method);
			shareAble.init(context);
			shareAble.shareTextWithImage(text, imageUrl, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final void releaseAuth(String method) {
		try {
			Shareable shareAble = storage.get(method);
			shareAble.releastAuth();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static {
		WeiboShare.register();
		TencentShare.register();
		RennShare.register();
	}
}
