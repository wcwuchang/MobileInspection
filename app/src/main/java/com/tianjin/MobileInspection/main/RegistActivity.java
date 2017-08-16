package com.tianjin.MobileInspection.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.SelectPicPopupWindow;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;

public class RegistActivity extends Activity implements OnClickListener{
	
	private ImageView mivHeader;
	
	private EditText metUserName;
	private EditText metPassword;
	private EditText metQuestion;
	private EditText metAnswer;
	private Button mbtnRegist;
	private Button mbtnCancel;
	
	// 相册选图标记
	private static final int REQUESTCODE_PICK = 0x10000;
	// 相机拍照标记
    private static final int REQUESTCODE_TAKE = 0x10001;
    // 图片裁切标记
    private static final int REQUESTCODE_CUTTING = 0x10002;
    // 头像文件名称
    private static final String IMAGE_FILE_NAME = "header.jpg";
    // 头像图片本地路径
    private String urlpath;			
	// 自定义的头像编辑弹出框
	private SelectPicPopupWindow menuWindow; 
	
	private OnClickListener menuListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.takePhotoBtn:
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//下面这句指定调用相机拍照后的照片存储的路径
				takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, 
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
				startActivityForResult(takeIntent, REQUESTCODE_TAKE);
				break;
			case R.id.pickPhotoBtn:
				Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
				// 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
				pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(pickIntent, REQUESTCODE_PICK);
				break;
			case R.id.cancelBtn:
				
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rgist);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mivHeader=(ImageView)findViewById(R.id.img_regist_header);
		
		metUserName=(EditText)findViewById(R.id.edt_regist_name);
		metPassword=(EditText)findViewById(R.id.edt_regist_password);
		metQuestion=(EditText)findViewById(R.id.edt_regist_confidentiality_issues);
		metAnswer=(EditText)findViewById(R.id.edt_regist_confidentiality_issues_key);
		
		mbtnRegist=(Button)findViewById(R.id.btn_regist_sure);
		mbtnCancel=(Button)findViewById(R.id.btn_regist_cancel);
		
		mivHeader.setOnClickListener(this);
		mbtnCancel.setOnClickListener(this);
		mbtnRegist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_regist_header:
			menuWindow=new SelectPicPopupWindow(this, menuListener);
			menuWindow.showAtLocation(findViewById(R.id.layout_regist), 
					Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.btn_regist_sure:
			if(isCheck()){
				
			}
			break;
		case R.id.btn_regist_cancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	private boolean isCheck() {
		// TODO Auto-generated method stub
		if(CheckUntil.checkEditext(metUserName.getText()).equals("")){
			Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show();
			return false;
		}
		if(CheckUntil.checkEditext(metPassword.getText()).equals("")){
			Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
			return false;
		}
//		if(CheckUntil.checkEditext(metQuestion.getText()).equals("")){
//			Toast.makeText(this, "请输入保密问题", Toast.LENGTH_LONG).show();
//			return false;
//		}
//		if(CheckUntil.checkEditext(metAnswer.getText()).equals("")){
//			Toast.makeText(this, "请输入保密答案", Toast.LENGTH_LONG).show();
//			return false;
//		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {
			case REQUESTCODE_PICK:
				if(data!=null){
					startPhotoZoom(data.getData());
				}
				break;
			case REQUESTCODE_TAKE:
				File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(temp));
				break;
			case REQUESTCODE_CUTTING:
				if (data != null) {
					setPicToView(data);
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			// 取得SDCard图片路径做显示
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(null, photo);
			urlpath = FileUtil.saveFile(this, MyApplication.IMAGEFILEPATH,IMAGE_FILE_NAME, photo);
			mivHeader.setImageDrawable(drawable);

		}
	}
}
