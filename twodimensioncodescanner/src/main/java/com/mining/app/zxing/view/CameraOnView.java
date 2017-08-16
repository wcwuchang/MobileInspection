package com.mining.app.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.mining.app.zxing.camera.CameraManager;
import com.scanner.smpcapture.R;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public final class CameraOnView extends View {

	private String  TAG="ViewfinderView";
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;
	private int ScreenRate;
	private static final int CORNER_WIDTH = 5;
	private static final int MIDDLE_LINE_WIDTH = 6;
	private static final int MIDDLE_LINE_PADDING = 5;
	private static final int SPEEN_DISTANCE = 5;
	private static float density;
	private static final int TEXT_SIZE = 16;
	private static final int TEXT_PADDING_TOP = 30;
	private Paint paint;
	private int slideTop;
	private int slideBottom;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;
	boolean isFirst;
	private int mPreviewWidth ;
	private int mPreviewHeight ;
	private int mFrameW = 600;
	private int mFrameH = 600;
	private Handler handler = new Handler();
	private final static int TIMER_PERIOD = 6;
	private DecimalFormat format=new DecimalFormat("0");
	
	public CameraOnView(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = context.getResources().getDisplayMetrics().density;
		ScreenRate = (int)(15 * density);
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(10);
		Timer timer = new Timer(false);
		timer.schedule(new MyTimer(), TIMER_PERIOD, TIMER_PERIOD);
	}

	public void setPreviewSize(int w, int h) {
		mPreviewWidth = w;
		mPreviewHeight = h;
		int minSize = mPreviewWidth < mPreviewHeight ? mPreviewWidth : mPreviewHeight;
		if (minSize >= 1080) {
			mFrameW = 600;
			mFrameH = 600;
		} else if (minSize >= 720) {
			mFrameW = 500;
			mFrameH = 500;
		} else if (minSize >= 480) {
			mFrameW = 400;
			mFrameH = 400;
		} else {
			mFrameW = mPreviewWidth;
			mFrameH = mPreviewHeight;
		}
		CameraManager.get().adjustmentCameraSize(mPreviewWidth, mPreviewHeight);
	}

	@Override
	public void onDraw(Canvas canvas) {
		setPreviewSize(canvas.getWidth(), canvas.getHeight());
		int	left = mPreviewWidth / 2 - mFrameW / 2;
		int	top = mPreviewHeight / 2 - mFrameH / 2;
		int	right = mPreviewWidth / 2 + mFrameW / 2;
		int	bottom = mPreviewHeight / 2 + mFrameH / 2;
		if(!isFirst){
			isFirst = true;
			slideTop = top;
			slideBottom = bottom;
		}
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, mPreviewWidth, top, paint);
		canvas.drawRect(0, top,left, bottom + 1, paint);
		canvas.drawRect(right + 1, top, mPreviewWidth, bottom + 1, paint);
		canvas.drawRect(0, bottom + 1, mPreviewWidth, mPreviewHeight, paint);
		if (resultBitmap != null) {
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, left, top, paint);
		} else {
			//扫描框四个角
			paint.setColor(Color.GREEN);
			canvas.drawRect(left, top, left + ScreenRate, top + CORNER_WIDTH, paint);
			canvas.drawRect(left, top, left + CORNER_WIDTH, top + ScreenRate, paint);
			canvas.drawRect(right - ScreenRate, top, right, top + CORNER_WIDTH, paint);
			canvas.drawRect(right - CORNER_WIDTH,top,right, top + ScreenRate, paint);
			canvas.drawRect(left, bottom - CORNER_WIDTH, left + ScreenRate, bottom, paint);
			canvas.drawRect(left, bottom - ScreenRate, left + CORNER_WIDTH, bottom, paint);
			canvas.drawRect(right - ScreenRate, bottom - CORNER_WIDTH, right, bottom, paint);
			canvas.drawRect(right - CORNER_WIDTH, bottom - ScreenRate, right, bottom, paint);

			slideTop += SPEEN_DISTANCE;
			if(slideTop >= bottom){
				slideTop = top;
			}
			//扫描线
			Rect lineRect = new Rect();  
            lineRect.left = left;
            lineRect.right = right;
            lineRect.top = slideTop;  
            lineRect.bottom = slideTop + 18;  
            canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap(), null, lineRect, paint);
           	//提示文字
            paint.setTextSize(TEXT_SIZE * density);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
			String text = "将二维码放入框内, 即可自动扫描";
			float textWidth;
			paint.setColor(Color.WHITE);
			textWidth = paint.measureText(text);
            canvas.drawText(text, (mPreviewWidth - textWidth)/2, (float) (bottom + paint.getTextSize()*5), paint);
				Collection<ResultPoint> currentPossible = possibleResultPoints;
				Collection<ResultPoint> currentLast = lastPossibleResultPoints;
				if (currentPossible.isEmpty()) {
					lastPossibleResultPoints = null;
				} else {
					possibleResultPoints = new HashSet<ResultPoint>(8);
					lastPossibleResultPoints = currentPossible;
					paint.setAlpha(OPAQUE);
					paint.setColor(resultPointColor);
					for (ResultPoint point : currentPossible) {
						Log.d("POINT", "onDraw: " + (point.getX()) + " X " + (point.getY()));
						canvas.drawCircle(left + point.getX(), top + point.getY(), 8.0f, paint);
					}
				}
				if (currentLast != null) {
					paint.setAlpha(OPAQUE / 2);
					paint.setColor(resultPointColor);
					for (ResultPoint point : currentLast) {
						canvas.drawCircle(left + point.getX(), top + point.getY(), 8.0f, paint);
					}
				}
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	class MyTimer extends TimerTask {
		@Override
		public void run() {
			handler.post(new Runnable() {
				@Override
				public void run() {
						invalidate();
				}
			});
		}
	}

}
