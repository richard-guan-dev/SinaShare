package com.microsoft.guan.share.library;

import com.microsoft.guan.share.constants.Constants;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.PutFeedParam;
import com.renn.rennsdk.param.PutShareUrlParam;
import com.renn.rennsdk.param.PutStatusParam;

import android.app.Activity;
import android.app.ProgressDialog;


public class RennShare implements Shareable {

	private static final RennShare rennShare = new RennShare();
	private ProgressDialog mProgressDialog;
	private ShareCallBack listener;
	private Activity context;
	private RennClient rennClient;;
	private boolean needPostText;
	private boolean needPostImage;
	private String text;
	private String imageUrl;

	private RennShare() {
		needPostText = false;
		needPostImage = false;
	}

	public static RennShare getInstance() {
		return rennShare;
	}

	public static void register() {
		ShareBox.register("renren", rennShare);
	}

	@Override
	public void shareText(String text, ShareCallBack callback) {
		// TODO Auto-generated method stub
		listener = callback;
		this.text = text;
		if (rennClient.isAuthorizeValid() && !rennClient.isAuthorizeExpired()
				&& rennClient.isLogin()) {
			if (mProgressDialog == null) {
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setCancelable(true);
				mProgressDialog.setTitle("请等待");
				mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
				mProgressDialog.setMessage("正在分享");
				mProgressDialog.show();
			}
			PutStatusParam statusParams = new PutStatusParam();
			statusParams.setContent(text);
			try {
				rennClient.getRennService().sendAsynRequest(statusParams,
						new CallBack() {

							@Override
							public void onFailed(String arg0, String arg1) {
								// TODO Auto-generated method stub
								mProgressDialog.dismiss();
								mProgressDialog = null;
								listener.onError(arg0, arg1);
							}

							@Override
							public void onSuccess(RennResponse arg0) {
								// TODO Auto-generated method stub
								mProgressDialog.dismiss();
								mProgressDialog = null;
								listener.onSuccess(arg0);
							}
						});
			} catch (RennException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mProgressDialog.dismiss();
				mProgressDialog = null;
				listener.onError(e);
			}
		} else {
			getAuth();
		}
	}

	@Override
	public void shareTextWithImage(String text, String imageUrl,
			ShareCallBack callback) {
		// TODO Auto-generated method stub
		listener = callback;
		this.text = text;
		this.imageUrl = imageUrl;
		if (rennClient.isAuthorizeValid() && !rennClient.isAuthorizeExpired()
				&& rennClient.isLogin()) {
			PutShareUrlParam param = new PutShareUrlParam();
			param.setComment(text);
			param.setUrl(imageUrl);
			if (mProgressDialog == null) {
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setCancelable(true);
				mProgressDialog.setTitle("请等待");
				mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
				mProgressDialog.setMessage("正在分享");
				mProgressDialog.show();
			}
			try {
				rennClient.getRennService().sendAsynRequest(param,
						new CallBack() {
							@Override
							public void onSuccess(RennResponse response) {
								// TODO Auto-generated method stub
								mProgressDialog.dismiss();
								mProgressDialog = null;
								listener.onSuccess(response);
							}

							@Override
							public void onFailed(String errorCode,
									String errorMessage) {
								// TODO Auto-generated method stub
								mProgressDialog.dismiss();
								mProgressDialog = null;
								listener.onError(errorCode, errorMessage);
							}

						});
			} catch (RennException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mProgressDialog.dismiss();
				mProgressDialog = null;
				listener.onError(e);
			}
		} else {
			getAuth();
		}

	}

	@Override
	public void init(Activity context) {
		// TODO Auto-generated method stub
		this.context = context;
		this.rennClient = RennClient.getInstance(context);
	}

	@Override
	public void getAuth() {
		// TODO Auto-generated method stub
		rennClient.init(Constants.RENREN_APP_ID, Constants.RENREN_API_KEY,
				Constants.RENREN_SECRET_KEY);
		rennClient
				.setScope("read_user_blog read_user_photo read_user_status read_user_album "
						+ "read_user_comment read_user_share publish_blog publish_share "
						+ "send_notification photo_upload status_update create_album "
						+ "publish_comment publish_feed");
		rennClient.setTokenType("bearer");
		rennClient.setLoginListener(new LoginListener() {

			@Override
			public void onLoginCanceled() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoginSuccess() {
				// TODO Auto-generated method stub
				if (needPostImage) {
					needPostImage = false;
					shareTextWithImage(text, imageUrl, listener);
				}

				if (needPostText) {
					needPostText = false;
					shareText(text, listener);
				}
			}

		});
		rennClient.login(context);
	}

	@Override
	public void releastAuth() {
		// TODO Auto-generated method stub
		rennClient.logout();
	}

}
