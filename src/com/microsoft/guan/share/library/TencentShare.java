package com.microsoft.guan.share.library;

import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.guan.share.constants.Constants;
import com.microsoft.guan.sina.R;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;
import com.tencent.weibo.webview.listener.TencentDialogListener;
import com.weibo.net.DialogError;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class TencentShare implements Shareable {

	private OAuthV2 oAuth; // Oauth鉴权所需及所得信息的封装存储单元
	private TAPI tAPI;
	private ShareCallBack listener;
	private static final TencentShare tencentShare = new TencentShare();
	private final OauthHandler oauthHandler = new OauthHandler();
	private boolean needPost = false;
	private boolean hasImage;
	private boolean hasOauth;
	private String text;
	private String imageUrl;

	private Activity context;

	private TencentShare() {
		needPost = false;
		hasOauth = false;
	}

	public static TencentShare getInstance() {
		return tencentShare;
	}

	public static void register() {
		ShareBox.register("tencent", tencentShare);
	}

	@Override
	public void shareText(String text, ShareCallBack listener) {
		// TODO Auto-generated method stub
		this.text = text;
		needPost = true;
		hasImage = false;
		this.listener = listener;

		if (hasOauth) {
			oauthHandler.sendEmptyMessage(1);
		} else
			getAuth();
	}

	@Override
	public void shareTextWithImage(String text, String imageUrl,
			ShareCallBack listener) {
		// TODO Auto-generated method stub
		this.text = text;
		this.imageUrl = imageUrl;
		needPost = true;
		hasImage = true;
		this.listener = listener;
		if (hasOauth) {
			oauthHandler.sendEmptyMessage(1);
		} else
			getAuth();
	}

	@Override
	public void init(Activity activity) {
		// TODO Auto-generated method stub
		this.context = activity;

	}

	public void getAuth() {
		oAuth = new OAuthV2(Constants.TENCENT_REDIRECT_URL);
		oAuth.setClientId(Constants.TENCENT_APP_KEY);
		oAuth.setClientSecret(Constants.TENCENT_APP_SECRET);
		Dialog dialog = new OAuthV2AuthorizeWebView((Context) context,
				R.style.ContentOverlay, oAuth, new TencentListener());
		dialog.show();
		WindowManager windowManager = context.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);

	}

	class TencentListener implements TencentDialogListener {

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			try {
				oAuth.setAccessToken(values.getString("access_token"));
				oAuth.setExpiresIn(values.getString("expires_in"));
				oAuth.setOpenid(values.getString("openid"));
				oAuth.setOpenkey(values.getString("openkey"));

			} catch (Exception e) {
				e.printStackTrace();
			}
			hasOauth = true;
			if (needPost) {
				Message msg = new Message();
				msg.what = 1;
				oauthHandler.sendMessage(msg);
			}
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onWeiboException(Exception e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(Exception e) {
			// TODO Auto-generated method stub

		}

	}

	class OauthHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				new Thread() {
					String response = "";

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						super.run();
						tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
						try {
							if (!hasImage)
								response = tAPI.add(oAuth, "json", text,
										"127.0.0.1");
							else
								response = tAPI.addPic(oAuth, "json", text,
										"127.0.0.1", imageUrl);
							tAPI.shutdownConnection();
							Message msg = new Message();
							msg.obj = response;
							msg.what = 2;
							oauthHandler.sendMessage(msg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							listener.onError(e);
						}

					}

				}.start();
				needPost = false;
				break;
			case 2:
				JSONObject json;
				try {
					json = new JSONObject((String) msg.obj);
					// Toast.makeText(
					// context.getApplicationContext(),
					// json.getString("msg") + " "
					// + json.getString("errcode"),
					// Toast.LENGTH_LONG).show();
					listener.onSuccess("");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onError(e);
				}

				break;
			}

		}

	}

	@Override
	public void releastAuth() {
		// TODO Auto-generated method stub
		
	}

}
