package com.microsoft.guan.share.library;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.guan.share.constants.Constants;
import com.microsoft.guan.sina.R;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.DialogError;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;
import com.weibo.net.AsyncWeiboRunner.RequestListener;

public class WeiboShare implements Shareable {

	private Weibo weibo;
	private Activity context;
	private final OauthHandler oauthHandler = new OauthHandler();
	private ShareCallBack listener;

	private String text;
	private String imageUrl;
	private boolean hasImage;
	private boolean needPost = false;
	private static final WeiboShare weiboShare = new WeiboShare();

	public static WeiboShare getInstance() {
		return weiboShare;
	}

	public static final void register() {
		ShareBox.register("sina", weiboShare);
	}

	public void init(Activity context) {
		this.context = context;

		weibo = Weibo.getInstance();
	}

	private WeiboShare() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void shareText(final String text, ShareCallBack listener) {
		// TODO Auto-generated method stub
		this.text = text;
		hasImage = false;
		needPost = true;
		this.listener = listener;
		if (weibo.isSessionValid()) {
			oauthHandler.sendEmptyMessage(1);
		} else {
			getAuth();
		}
	}

	@Override
	public void shareTextWithImage(final String text, final String imageUrl,
			ShareCallBack listener) {
		// TODO Auto-generated method stub
		this.text = text;
		this.imageUrl = imageUrl;
		hasImage = true;
		needPost = true;
		this.listener = listener;
		if (weibo.isSessionValid()) {
			oauthHandler.sendEmptyMessage(1);
		} else {
			getAuth();
		}

	}

	public void getAuth() {
		// TODO Auto-generated method stub
		// weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(Constants.WEIBO_APP_KEY,
				Constants.WEIBO_APP_SECRET);
		weibo.setRedirectUrl(Constants.WEIBO_REDIRECT_URL);
		weibo.authorize(context, new WeiboDialogListener() {

			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				String token = values.getString("access_token");
				String expires_in = values.getString("expires_in");
				AccessToken accessToken = new AccessToken(token,
						Constants.WEIBO_APP_SECRET);
				accessToken.setExpiresIn(expires_in);
				Weibo.getInstance().setAccessToken(accessToken);
				Log.d("token", accessToken.getToken());
				if (needPost) {
					Message msg = new Message();
					msg.what = 1;
					oauthHandler.sendMessage(msg);
				}
			}

			@Override
			public void onError(DialogError e) {
				listener.onError(e);
			}

			@Override
			public void onCancel() {
				Toast.makeText(context, "Auth cancel", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onWeiboException(WeiboException e) {
				listener.onError(e);
			}

		});

	}

	class OauthHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				final ShareListener share = new ShareListener();
				try {
					if (!hasImage)
						new Thread() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								super.run();
								try {
									share.update(weibo,
											Constants.WEIBO_APP_KEY, text, "",
											"");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									listener.onError(e);
								}

							}
						}.start();

					else
						new Thread() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								super.run();
								try {
									share.upload(weibo,
											Constants.WEIBO_APP_KEY, imageUrl,
											text, "", "");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									listener.onError(e);
								}

							}
						}.start();
					needPost = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onError(e);
				}

				break;

			}
		}

	}

	class ShareListener implements RequestListener {

		public String upload(Weibo weibo, String source, String picUrl,
				String status, String lon, String lat) throws WeiboException {
			WeiboParameters bundle = new WeiboParameters();
			bundle.add("source", source);
			bundle.add("url", picUrl);
			bundle.add("status", status);
			if (!TextUtils.isEmpty(lon)) {
				bundle.add("lon", lon);
			}
			if (!TextUtils.isEmpty(lat)) {
				bundle.add("lat", lat);
			}
			String rlt = "";
			String url = Weibo.SERVER + "statuses/upload_url_text.json";
			AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
			weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST,
					this);

			return rlt;
		}

		public String update(Weibo weibo, String source, String status,
				String lon, String lat) throws MalformedURLException,
				IOException, WeiboException {
			WeiboParameters bundle = new WeiboParameters();
			bundle.add("source", source);
			bundle.add("status", status);
			if (!TextUtils.isEmpty(lon)) {
				bundle.add("lon", lon);
			}
			if (!TextUtils.isEmpty(lat)) {
				bundle.add("lat", lat);
			}
			String rlt = "";
			String url = Weibo.SERVER + "statuses/update.json";
			AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
			weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST,
					this);
			return rlt;
		}

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			listener.onSuccess(response);
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub
			listener.onError(e);
		}

		@Override
		public void onError(final WeiboException e) {
			// TODO Auto-generated method stub
			listener.onError(e);
		}
	}

	@Override
	public void releastAuth() {
		// TODO Auto-generated method stub
		
	}

}
