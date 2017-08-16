package com.scanner.smpcapture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.CameraOnView;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class ERCodeScannerActivity extends Activity implements Callback , View.OnClickListener{

	private String TAG="ERCodeScannerActivity";
	private CaptureActivityHandler handler;
	private CameraOnView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private Camera camera;
	private SurfaceView surfaceView;
	private boolean decode=false;//是否扫描出结果标记 false未有结果，true 已出结果
	private SoundPool mSPool;//扫描结果音效
	private int mSound_id;
	private Timer mAFTimer;
	private boolean mNaiveAF=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
				WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.activity_capture);
		initData();
		initView();
	}

	private void initData() {
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	private void initView() {
		viewfinderView = (CameraOnView) findViewById(R.id.viewfinder_view);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		viewfinderView.setOnClickListener(this);
		findViewById(R.id.rela_p).setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.rela_p) {
			if (mNaiveAF) {
				stopAutoFocus();
			} else {
				startAutoFocus();
			}

		} else {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
		mSPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
		mSound_id = mSPool.load(this, R.raw.sound, 1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		mSPool.release();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		String resultString = result.getText();
		decode=true;
		Log.d(TAG, "handleDecode: "+resultString);
		onResultHandler(resultString);
	}

	/**
	 * 显示扫描结果
	 * @param resultString
	 */
	private void onResultHandler(final String resultString){
		if(TextUtils.isEmpty(resultString)){
			Toast.makeText(ERCodeScannerActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			decode=false;
			return;
		}
		mSPool.play(mSound_id, 1.0F, 1.0F, 0, 0, 1.0F);
		Intent intent=getIntent();
		intent.putExtra("result",resultString);
		setResult(RESULT_OK,intent);
		finish();
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//		alertDialogBuilder.setTitle("扫 描 结 果");
//		alertDialogBuilder.setMessage(resultString);
//
//		String chose=null;
//		if(UrlUtil.isUri(resultString)){
//			chose="进入网址";
//			alertDialogBuilder.setPositiveButton(chose,
//					new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(resultString));
//							startActivity(i);
//							decode=false;
//						}
//					});
//		}else{
//			chose="确定";
//			alertDialogBuilder.setPositiveButton(chose, null);
//		}
//
//		alertDialogBuilder.setNegativeButton("取消", null);
//
//		alertDialogBuilder.setCancelable(true);
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//					@Override
//					public void onDismiss(DialogInterface dialog) {
//						decode=false;
//					}
//				});
//		alertDialog.show();
	}

	/**
	 * 初始化相机
	 * @param surfaceHolder
     */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			camera=CameraManager.get().getCamera();
			Camera.Parameters p=camera.getParameters();
			CameraManager.get().setCameraPictureSize();
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public CameraOnView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	/**
	 * 聚焦
	 */
	public void startAutoFocus() {
		AutoFocusTask task = new AutoFocusTask();
		if (mAFTimer != null) {
			mAFTimer.cancel();
		}
		mAFTimer = new Timer();
		mAFTimer.schedule(task, 300, 3000);
		this.mNaiveAF = true;
	}

	public void stopAutoFocus() {
		if (mAFTimer != null) {
			mAFTimer.cancel();
			mAFTimer = null;
		}
		camera=CameraManager.get().getCamera();
		if (camera != null) {
			camera.cancelAutoFocus();
			Camera.Parameters params = camera.getParameters();
			List<String> focusModes = params.getSupportedFocusModes();
			if (focusModes != null) {
				if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
					params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
					camera.setParameters(params);
				}
			}
		}
		this.mNaiveAF = false;
	}

	private class AutoFocusTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			camera=CameraManager.get().getCamera();
			if (camera != null) {
				Camera.Parameters params = camera.getParameters();
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				camera.setParameters(params);
				camera.autoFocus(null);
				Log.d(TAG, "AutoFocusTask");
			}
		}

	}

	/**
	 * 返回是否已经解析出结果
	 * @return
	 *    true 解析
	 *    false 未解析
     */
	public boolean getDecode(){
		return decode;
	}

}