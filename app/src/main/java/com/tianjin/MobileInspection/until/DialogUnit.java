package com.tianjin.MobileInspection.until;

import android.content.Context;

import com.tianjin.MobileInspection.customview.dialog.CustomDialog;

/**
 * Created by zhuangaoran on 2016-12-29.
 *
 * 网络请求加载上传弹框
 */
public class DialogUnit {

    private static CustomDialog dialog;

    public static void showDialog(Context context){
            dialog = new CustomDialog(context, "");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();
    }

    public static void closeDialog(){
        if (dialog.isShowing()){
            dialog.cancel();
        }
    }

}
