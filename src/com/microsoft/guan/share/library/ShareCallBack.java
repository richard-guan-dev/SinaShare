package com.microsoft.guan.share.library;

import com.renn.rennsdk.RennResponse;
import com.weibo.net.DialogError;

public interface ShareCallBack {
	void onSuccess(String response);
	void onError(Exception e);
	void onError(DialogError e);
	void onSuccess(RennResponse arg0);
	void onError(String arg0, String arg1);
}
