package com.hualu.zlib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.hualu.zlib.R;
import com.hualu.zlib.decode.DecodeThread;

public class SignResultActivity extends Activity {

	private TextView tvBack;
	private TextView tvStructName;
	private ImageView ivStatusImage;
	private TextView tvStatusMessage;
	private TextView tvSignPerson;
	private TextView tvSignTime;
	private TextView tvSignCompany;
	private TextView tvPreSignTime;
	private TextView tvPreSignPerson;
	private TextView tvAdd;

	public static final String ADD_CHECKER_LIST = "add_checker_list";
	public static final String EVENT_TYPE = "event_type";
	private static final String STRUCT_NAME = "struct_name";
	private static final String IS_SUCCESSFUL = "is_successful";
	private static final String SIGN_PERSON = "sign_person";
	private static final String SIGN_TIME = "sign_time";
	private static final String SIGN_COMPANY = "sign_company";
	private static final String PRE_SIGN_TIME = "pre_sign_time";
	private static final String PRE_SIGN_PERSON = "pre_sign_person";
	private static final String SIGN_MESSAGE = "sign_message";

	public static Intent getIntent(Activity activity, String structName, boolean isSuccessful,
			String signPerson, String signTime, String signCompany, String preSignTime,
			String preSignPerson, String signMessage) {
		Intent i = new Intent(activity, SignResultActivity.class);
		i.putExtra(STRUCT_NAME, structName);
		i.putExtra(IS_SUCCESSFUL, isSuccessful);
		i.putExtra(SIGN_PERSON, signPerson);
		i.putExtra(SIGN_TIME, signTime);
		i.putExtra(SIGN_COMPANY, signCompany);
		i.putExtra(PRE_SIGN_TIME, preSignTime);
		i.putExtra(PRE_SIGN_PERSON, preSignPerson);
		i.putExtra(SIGN_MESSAGE, signMessage);
		return i;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_result);

		Bundle extras = getIntent().getExtras();

		tvBack = (TextView) findViewById(R.id.tv_back);
		tvStructName = (TextView) findViewById(R.id.tv_struct_name);
		ivStatusImage = (ImageView) findViewById(R.id.iv_sign_status_image);
		tvStatusMessage = (TextView) findViewById(R.id.tv_sign_status_message);
		tvSignPerson = (TextView) findViewById(R.id.tv_sign_person);
		tvSignTime = (TextView) findViewById(R.id.tv_sign_time);
		tvSignCompany = (TextView) findViewById(R.id.tv_sign_company);
		tvPreSignTime = (TextView) findViewById(R.id.tv_pre_sign_time);
		tvPreSignPerson = (TextView) findViewById(R.id.tv_pre_sign_person);
		tvAdd = (TextView) findViewById(R.id.tv_add);

		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				onBackPressed();
			}
		});

		tvAdd.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(EVENT_TYPE, ADD_CHECKER_LIST);
				setResult(RESULT_OK, i);
				onBackPressed();
			}
		});

		if (null != extras) {
			String structName = extras.getString(STRUCT_NAME);
			boolean isSuccessful = extras.getBoolean(IS_SUCCESSFUL);
			String signPerson = extras.getString(SIGN_PERSON);
			String signTime = extras.getString(SIGN_TIME);
			String signCompany = extras.getString(SIGN_COMPANY);
			String preSignTime = extras.getString(PRE_SIGN_TIME);
			String preSignPerson = extras.getString(PRE_SIGN_PERSON);
			String signMessage = extras.getString(SIGN_MESSAGE);

			tvStructName.setText(orEmpty(structName));
			tvSignPerson.setText(orEmpty(signPerson));
			tvSignTime.setText(orEmpty(signTime));
			tvSignCompany.setText(orEmpty(signCompany));
			tvPreSignTime.setText(orEmpty(preSignTime));
			tvPreSignPerson.setText(orEmpty(preSignPerson));

			tvStatusMessage.setText(orEmpty(signMessage));
			if (isSuccessful) {
				ivStatusImage.setImageResource(R.drawable.sign_ok);
			} else {
				ivStatusImage.setImageResource(R.drawable.sign_error);
			}
		}
	}

	private String orEmpty(String s) {
		return s != null ? s : "";
	}
}
