/*
 * Copyright (C) 2010 ZXing authors
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
import android.graphics.Point;
import android.hardware.Camera;
import android.media.CameraProfile;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

final class CameraConfigurationManager {

  private static final String TAG ="CameraConfigurationManager";

  private static final int TEN_DESIRED_ZOOM = 27;
  private static final int DESIRED_SHARPNESS = 30;

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  private final Context context;
  private Point screenResolution;
  private Point cameraResolution;
  private int previewFormat;
  private String previewFormatString;
  private boolean inittDesired=false;

  CameraConfigurationManager(Context context) {
    this.context = context;
  }

  void initFromCameraParameters(Camera camera) {
    if(camera==null) return;
      Camera.Parameters parameters = camera.getParameters();
      previewFormat = parameters.getPreviewFormat();
      previewFormatString = parameters.get("preview-format");
      Log.d("parameters", "initFromCameraParameters: "+previewFormatString);
      WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = manager.getDefaultDisplay();
      screenResolution = new Point(display.getWidth(), display.getHeight());
      cameraResolution = getCameraResolution(parameters, screenResolution);
  }

  void setDesiredCameraParameters(Camera camera) {
    if(camera==null) return;
      Camera.Parameters parameters = camera.getParameters();
      parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
      parameters.setJpegQuality(CameraProfile.QUALITY_HIGH);
      setFlash(parameters);
      setZoom(parameters);
      setDisplayOrientation(camera, 90);
      camera.setParameters(parameters);
  }

  Point getCameraResolution() {
    return cameraResolution;
  }

  Point getScreenResolution() {
    return screenResolution;
  }

  int getPreviewFormat() {
    return previewFormat;
  }

  String getPreviewFormatString() {
    return previewFormatString;
  }

  private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {

    String previewSizeValueString = parameters.get("preview-size-values");
    // saw this on Xperia
    if (previewSizeValueString == null) {
      previewSizeValueString = parameters.get("preview-size-value");
    }
    Log.d("size", "previewSizeValueString: "+previewSizeValueString);
    Point cameraResolution = null;

    if (previewSizeValueString != null) {
      cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
    }

    if (cameraResolution == null) {
      // Ensure that the camera resolution is a multiple of 8, as the screen may not be.
      cameraResolution = new Point((screenResolution.x >> 3) << 3,(screenResolution.y >> 3) << 3);
    }
    Log.d("size", "screenResolution2: "+screenResolution.x+" X "+screenResolution.y);
    Log.d("size", "cameraResolution2: "+cameraResolution.x+" X "+cameraResolution.y);
    return cameraResolution;
  }

  private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
    int bestX = 0;
    int bestY = 0;
    int diff = Integer.MAX_VALUE;
    int cw=10000;
    int ch=10000;
    for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {
      previewSize = previewSize.trim();
      int dimPosition = previewSize.indexOf('x');
      if (dimPosition < 0) {
        continue;
      }
      int newX;
      int newY;
      try {
        newX = Integer.parseInt(previewSize.substring(0, dimPosition));
        newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
      } catch (NumberFormatException nfe) {
        continue;
      }
      //camera的宽高与屏幕的高宽是相对应的
      //此处camera的大小适配取对应宽高的差值最小的尺寸
      if(cw>=Math.abs(newX - screenResolution.y)&&ch>=Math.abs(newY - screenResolution.x)){
        bestX=newX;
        bestY=newY;
        cw=Math.abs(newX - screenResolution.y);
        ch=Math.abs(newY - screenResolution.x);
      }
    }
    Log.d("size", "bast size: "+bestX+" X "+bestY);
    if (bestX > 0 && bestY > 0) {
      return new Point(bestX, bestY);
    }
    return null;
  }

  private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
    int tenBestValue = 0;
    for (String stringValue : COMMA_PATTERN.split(stringValues)) {
      stringValue = stringValue.trim();
      double value;
      try {
        value = Double.parseDouble(stringValue);
      } catch (NumberFormatException nfe) {
        return tenDesiredZoom;
      }
      int tenValue = (int) (10.0 * value);
      if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
        tenBestValue = tenValue;
      }
    }
    return tenBestValue;
  }

  private void setFlash(Camera.Parameters parameters) {
    // FIXME: This is a hack to turn the flash off on the Samsung Galaxy.
    // And this is a hack-hack to work around a different value on the Behold II
    // Restrict Behold II check to Cupcake, per Samsung's advice
    //if (Build.MODEL.contains("Behold II") &&
    //    CameraManager.SDK_INT == Build.VERSION_CODES.CUPCAKE) {
    if (Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) { // 3 = Cupcake
      parameters.set("flash-value", 1);
    } else {
      parameters.set("flash-value", 2);
    }
    // This is the standard setting to turn the flash off that all devices should honor.
    parameters.set("flash-mode", "off");
  }

  private void setZoom(Camera.Parameters parameters) {

    String zoomSupportedString = parameters.get("zoom-supported");
    if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
      return;
    }

    int tenDesiredZoom = TEN_DESIRED_ZOOM;

    String maxZoomString = parameters.get("max-zoom");
    if (maxZoomString != null) {
      try {
        int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
      }
    }

    String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
    if (takingPictureZoomMaxString != null) {
      try {
        int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
        if (tenDesiredZoom > tenMaxZoom) {
          tenDesiredZoom = tenMaxZoom;
        }
      } catch (NumberFormatException nfe) {
      }
    }

    String motZoomValuesString = parameters.get("mot-zoom-values");
    if (motZoomValuesString != null) {
      tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
    }

    String motZoomStepString = parameters.get("mot-zoom-step");
    if (motZoomStepString != null) {
      try {
        double motZoomStep = Double.parseDouble(motZoomStepString.trim());
        int tenZoomStep = (int) (10.0 * motZoomStep);
        if (tenZoomStep > 1) {
          tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
        }
      } catch (NumberFormatException nfe) {
        // continue
      }
    }

    // Set zoom. This helps encourage the user to pull back.
    // Some devices like the Behold have a zoom parameter
    if (maxZoomString != null || motZoomValuesString != null) {
      parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
    }

    // Most devices, like the Hero, appear to expose this zoom parameter.
    // It takes on values like "27" which appears to mean 2.7x zoom
    if (takingPictureZoomMaxString != null) {
      parameters.set("taking-picture-zoom", tenDesiredZoom);
    }
  }

	public static int getDesiredSharpness() {
		return DESIRED_SHARPNESS;
	}
	
	/**
	 * compatible  1.6
	 * @param camera
	 * @param angle
	 */
	protected void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;  
        try  
        {  
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });  
            if (downPolymorphic != null)  
                downPolymorphic.invoke(camera, new Object[] { angle });  
        }  
        catch (Exception e1)  
        {  
        }  
   }  

}
