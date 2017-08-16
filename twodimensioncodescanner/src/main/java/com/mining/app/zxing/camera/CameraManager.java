/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mining.app.zxing.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * 此对象包装相机服务对象，并希望是唯一一个与它说话的对象。
 * 封装的实现需要预览步骤大小的图像，这是用于预览和解码。
 */
public final class CameraManager {

    private static final String TAG = "CameraManager";

    //二维码扫描有效区域大小设置 最小 240*240 最大 600*600
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 600;
    private static final int MAX_FRAME_HEIGHT = 600;

    private static CameraManager cameraManager;

    static final int SDK_INT; // Later we can use Build.VERSION.SDK_INT

    static {
        int sdkInt;
        try {
            sdkInt = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException nfe) {
            // Just to be safe
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    private final Context context;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized = false;
    private boolean previewing;
    private final boolean useOneShotPreviewCallback;
    private Camera.Parameters parameters;

    private int cameraH;
    /**
     * 预览帧被传递到这里，我们传递给注册的处理程序。请务必清除处理程序，所以它只会收到一个消息。
     */
    private final PreviewCallback previewCallback;
    /**
     * 自动对焦回调到这里，并派出所要求的处理。
     */
    private final AutoFocusCallback autoFocusCallback;

    /**
     * 初始化静态对象。
     *
     * @param context The Activity which wants to use the camera.
     */
    public static void init(Context context) {
        if (cameraManager == null) {
            cameraManager = new CameraManager(context);
        }
    }

    /**
     * 获取 CameraManager 对象.
     *
     * @return A reference to the CameraManager singleton.
     */
    public static CameraManager get() {
        return cameraManager;
    }

    private CameraManager(Context context) {

        this.context = context;
        this.configManager = new CameraConfigurationManager(context);

        // Camera.setOneShotPreviewCallback() has a race condition in Cupcake, so we use the older
        // Camera.setPreviewCallback() on 1.5 and earlier. For Donut and later, we need to use
        // the more efficient one shot callback, as the older one can swamp the system and cause it
        // to run out of memory. We can't use SDK_INT because it was introduced in the Donut SDK.
        //useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.CUPCAKE;
        useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3; // 3 = Cupcake

        previewCallback = new PreviewCallback(configManager, useOneShotPreviewCallback);
        autoFocusCallback = new AutoFocusCallback();
    }

    /**
     * 打开摄像头驱动和初始化硬件参数。
     *
     * @param holder The surface object which the camera will draw preview frames into.
     * @throws IOException Indicates the camera driver failed to open.
     */
    public void openDriver(SurfaceHolder holder) throws IOException {
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);
            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera);
            }
            configManager.setDesiredCameraParameters(camera);
            FlashlightManager.enableFlashlight();
        }
    }

    /**
     * 如果仍然在使用关闭相机驱动程序。
     */
    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.release();
            camera = null;
        }
    }

    /**
     * 开启预览
     */
    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (camera != null && previewing) {
            if (!useOneShotPreviewCallback) {
                camera.setPreviewCallback(null);
            }
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            autoFocusCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    /**
     * A single preview frame will be returned to the handler supplied. The data will arrive as byte[]
     * in the message.obj field, with width and height encoded as message.arg1 and message.arg2,
     * respectively.
     *
     * @param handler The handler to send the message to.
     * @param message The what field of the message to be sent.
     */
    public void requestPreviewFrame(Handler handler, int message) {
        if (camera != null && previewing) {
            previewCallback.setHandler(handler, message);
            if (useOneShotPreviewCallback) {
                camera.setOneShotPreviewCallback(previewCallback);
            } else {
                camera.setPreviewCallback(previewCallback);
            }
        }
    }

    /**
     * Asks the camera hardware to perform an autofocus.
     *
     * @param handler The Handler to notify when the autofocus completes.
     * @param message The message to deliver.
     */
    public void requestAutoFocus(Handler handler, int message) {
        if (camera != null && previewing) {
            autoFocusCallback.setHandler(handler, message);
            //Log.d(TAG, "Requesting auto-focus callback");
            camera.autoFocus(autoFocusCallback);
        }
    }

    /**
     * Calculates the framing rect which the UI should draw to show the user where to place the
     * barcode. This target helps with alignment as well as forces the user to hold the device
     * far enough away to ensure the image will be in focus.
     *
     * @return The rectangle to draw on screen in window coordinates.
     */
    public Rect getFramingRect() {
        Point screenResolution = configManager.getScreenResolution();
        if (framingRect == null) {
            if (camera == null) {
                return null;
            }
            int width = screenResolution.x * 3 / 4;
            if (width < MIN_FRAME_WIDTH) {
                width = MIN_FRAME_WIDTH;
            } else if (width > MAX_FRAME_WIDTH) {
                width = MAX_FRAME_WIDTH;
            }
            int height = screenResolution.y * 3 / 4;
            if (height < MIN_FRAME_HEIGHT) {
                height = MIN_FRAME_HEIGHT;
            } else if (height > MAX_FRAME_HEIGHT) {
                height = MAX_FRAME_HEIGHT;
            }
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    /**
     * Like {@link #getFramingRect} but coordinates are in terms of the preview frame,
     * not UI / screen.
     */
    public Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect rect = new Rect(getFramingRect());
            Point cameraResolution = configManager.getCameraResolution();
            Point screenResolution = configManager.getScreenResolution();
            rect.left = rect.left * cameraResolution.y / screenResolution.x;
            rect.right = rect.right * cameraResolution.y / screenResolution.x;
            rect.top = rect.top * cameraResolution.x / screenResolution.y;
            rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            framingRectInPreview = rect;
        }
        return framingRectInPreview;
    }

    /**
     * A factory method to build the appropriate LuminanceSource object based on the format
     * of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        int previewFormat = configManager.getPreviewFormat();
        String previewFormatString = configManager.getPreviewFormatString();
        switch (previewFormat) {
            case PixelFormat.YCbCr_420_SP:
            case PixelFormat.YCbCr_422_SP:
                return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                        rect.width(), rect.height());
            default:
                if ("yuv420p".equals(previewFormatString)) {
                    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                            rect.width(), rect.height());
                }
        }
        throw new IllegalArgumentException("Unsupported picture format: " +
                previewFormat + '/' + previewFormatString);
    }

    public Context getContext() {
        return context;
    }

    public Camera.Size getCameraSize() {
        Camera.Parameters parameters = camera.getParameters();
        return parameters.getPreviewSize();
    }

    public Camera getCamera() {
        return camera;
    }

    public void adjustmentCameraSize(int w, int h) {
        if (camera != null) {
            if (cameraH != w) {
                Log.d("size", "beg: "+w+" X "+h);
                cameraH = w;
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
                    Camera.Size bast = null;
                    float st=(float)h/w;
                    Log.d("size", "st: "+st);
                    Log.d("size", "st: "+parameters.get("preview-size-values"));
                    for (Camera.Size size : sizes) {
                        Log.d("size", "sp: "+size.width+" X "+size.height);
                        if (bast == null) {
                            bast = size;
                        } else if(st<=((float)size.width/size.height)&&size.height>=w) {
                            bast = size;
                            Log.d("size", "s4t: "+((float)size.width/size.height));
                            Log.d("size", "bast: "+bast.width+" X "+bast.height);
                        }else {
                            continue;
                        }
                    }
                    Log.d("size", "adjustmentCameraSize: " + bast.width + " X " + bast.height);
                    parameters.setPreviewSize(bast.width, bast.height);
                    camera.setParameters(parameters);
                } catch (RuntimeException e) {
                    Log.d(TAG, "adjustmentCameraSize: error");
                }
                configManager.initFromCameraParameters(camera);
            }
        }
    }

    public void cameraSize() {
        if (camera != null) {
            Camera.Parameters p = camera.getParameters();
            Camera.Size s = p.getPreviewSize();
            Log.d(TAG, "cameraSize: " + s.width + " X " + s.height);
        }
    }

    private boolean isFlash = false;

    public void openLight() {
        Camera.Parameters p = camera.getParameters();
        if (isFlash) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
        isFlash = !isFlash;
        camera.setParameters(p);
    }

    public void setCameraPictureSize(){
        if(camera==null) return;
        Camera.Parameters parameters=camera.getParameters();
        Camera.Size p=parameters.getPreviewSize();
        List<Camera.Size> sizes=parameters.getSupportedPictureSizes();
        Camera.Size best=null;
        for(Camera.Size size:sizes){
            if(((float)p.width/p.height)==((float)size.width/size.height)){
                if(best==null) {
                    best = size;
                }else if(best.width<size.width){
                    best = size;
                }
            }

        }
        if(best!=null) {
            Log.d(TAG, "setCameraPictureSize: " + best.width + " X " + best.height);
            parameters.setPictureSize(best.width, best.height);
            camera.setParameters(parameters);
        }
    }

}
