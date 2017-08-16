package com.tianjin.MobileInspection.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.module.LoginModule;
import com.tianjin.MobileInspection.until.FileUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private String TAG="LoginActivity";

	private EditText medtName;
	private EditText medtPassword;
	private Button mbtnLogin;
	private ImageView mivCancelUsername;

	private String name;
	private String password;

	private LoginModule mLoginModule;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LoginModule.INT_LOGIN_SUCCESS:
				Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setAction("android.intent.action.INDEXACTIVITY");
				startActivity(intent);
				finish();
				break;
			case LoginModule.INT_LOGIN_FILED:
				Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		FileUtil.creatFile();
		initView();
		initDate();
	}

	private void initDate() {
		mLoginModule = new LoginModule(handler,this);
		name= MyApplication.getStringSharedPreferences("username","");
		password=MyApplication.getStringSharedPreferences("password","");
		medtName.setText(name);
		medtPassword.setText(password);
	}

	private void initView() {
		// TODO Auto-generated method stub
		medtName = (EditText) findViewById(R.id.edt_login_name);
		medtPassword = (EditText) findViewById(R.id.edt_login_password);
		mbtnLogin = (Button) findViewById(R.id.btn_login);

		mivCancelUsername=(ImageView)findViewById(R.id.iv_cancel_username);
		mbtnLogin.setOnClickListener(this);
		mivCancelUsername.setOnClickListener(this);

		medtName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!medtName.getText().toString().equals("")){
					mivCancelUsername.setVisibility(View.VISIBLE);
				}else{
					mivCancelUsername.setVisibility(View.GONE);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			if(checkData()){
				mLoginModule.login(name,password);
			}
			break;
		case R.id.iv_cancel_username:
			medtName.setText("");
			break;
		default:
			break;
		}
	}

	/**
	 * 校验用户名密码是否为空
	 * @return
     */
	private boolean checkData(){
		if(medtName.getText()==null||medtName.getText().toString().equals("")){
			Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		name=medtName.getText().toString();
		if(medtPassword.getText()==null||medtPassword.getText().toString().equals("")){
			Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		password=medtPassword.getText().toString();
		return true;
	}

}
