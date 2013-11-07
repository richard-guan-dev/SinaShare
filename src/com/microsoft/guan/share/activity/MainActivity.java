package com.microsoft.guan.share.activity;

import com.microsoft.guan.share.library.ShareBox;
import com.microsoft.guan.share.library.ShareCallBack;
import com.microsoft.guan.sina.R;
import com.renn.rennsdk.RennResponse;
import com.weibo.net.DialogError;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button text;
	private Button image;
	private EditText editText;
	private RadioGroup rg;
	private String method = "sina";
	private String imageUrl = "http://www.ibeike.com/images/logo.gif";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		text = (Button) findViewById(R.id.Text);
		image = (Button) findViewById(R.id.Image);
		editText = (EditText) findViewById(R.id.editText);
		rg = (RadioGroup) findViewById(R.id.radioGroup);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int radioButtonId) {
				// TODO Auto-generated method stub
				if (radioButtonId == R.id.Weibo) {
					image.setClickable(false);
					method = "sina";
					Log.d("checked", method);
				} else if (radioButtonId == R.id.Tencent) {
					image.setClickable(true);
					method = "tencent";
					Log.d("checked", method);

				} else if (radioButtonId == R.id.RenRen) {
					image.setClickable(true);
					method = "renren";
					Log.d("checked", method);

				}
			}

		});

		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareBox share = ShareBox.getInstance();
				share.init(MainActivity.this);
				share.setListener(new ShareListener());
				share.shareText(
						editText.getText().toString() + " "
								+ String.valueOf(Math.random()), method);
			};

		});

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareBox share = ShareBox.getInstance();
				share.init(MainActivity.this);
				share.setListener(new ShareListener());
				share.shareTextWithImage(editText.getText().toString() + " "
						+ String.valueOf(Math.random()), imageUrl, method);

			}

		});

		// ShareBox share = ShareBox.getInstance();
		// share.init(this);
		// share.shareText("Test From Other Class", "tencent");
		// share.shareTextWithImage("Test With Image",
		// "http://www.ibeike.com/images/logo.gif", "tencent");
	}

	class ShareListener implements ShareCallBack {

		@Override
		public void onSuccess(String response) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(Exception e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(RennResponse arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(String arg0, String arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	
}
