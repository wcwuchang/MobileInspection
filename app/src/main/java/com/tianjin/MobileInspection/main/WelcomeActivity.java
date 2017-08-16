package com.tianjin.MobileInspection.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.dialog.DownLoadDialog;
import com.tianjin.MobileInspection.entity.Info;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.service.PushService;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.FileUtil;
import com.tianjin.MobileInspection.until.PullPersonService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 欢迎页
 * Created by wuchang on 2016/9/26.
 */
public class WelcomeActivity extends AppCompatActivity implements DownLoadDialog.DownLoadListener {
    private String TAG = "WelcomeActivity";
    private int SDK_PERMISSION_REQUEST = 5623;
    private String permissionInfo;
    private boolean permission = false;
    private MyThread thread;
    private Info info;

    private Dialog dialog;
    private View view;
    private TextView mtvCenter;
    private Button mbtnCancel;
    private Button mbtnUpdate;

    private DownLoadDialog downDialog;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FileUploadModule.DOWNLOAD_SUCCESS:
                    String name=msg.obj.toString();
                    KLog.d(name);
                    testPullGetPersons(name);
                    break;
                case FileUploadModule.DOWNLOAD_FILED:
                    if(!thread.isAlive()) {
                        thread.start();
                    }
                    break;
            }
        }
    };

    public void testPullGetPersons(String path) {
        try {
            File file = new File(path);
            InputStream inStream = new FileInputStream(file);
            List<Info> infos = PullPersonService.getPersons(inStream);
            if(infos!=null && infos.size()>0){
                info=infos.get(0);
                int version= com.tianjin.MobileInspection.BuildConfig.VERSION_CODE;
                double v=Double.valueOf(info.getVersion());
                KLog.d("version="+version+";v="+v);
                if(v>version){
                    initDialog();
                }else {
                    if(!thread.isAlive()) {
                        thread.start();
                    }
                }
            }else {
                if(!thread.isAlive()) {
                    thread.start();
                }
            }
        }catch (Exception e){
            KLog.d(e.getMessage());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        thread = new MyThread();
    }

    /**
     * 更新检查
     */
    private void chackUpdata() {
        FileUploadModule.downLoadFile(this,handler, ConnectionURL.STR_UPDATE,"update");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    getPersimmions();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 登录
     * 根据状态进入登录页或首页
     */
    private void login() {
        String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
        if (jsessionid.equals("")) {
            gotoActivity("android.intent.action.LOGINACTIVITY");
        } else {
//            PushUntil.addAlias();
            Intent intent=new Intent(this, PushService.class);
            startService(intent);
            gotoActivity("android.intent.action.INDEXACTIVITY");
        }
    }

    private void gotoActivity(String action) {
        Intent intentf = new Intent();
        intentf.setAction(action);
        startActivity(intentf);
        finish();
    }


    /**
     * 权限申请
     */
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            // 读取电话权限
            if (addPermission(permissions, Manifest.permission.CALL_PHONE)) {
                permissionInfo += "Manifest.permission.CALL_PHONE Deny \n";
            }
            // 发生短信
            if (addPermission(permissions, Manifest.permission.SEND_SMS)) {
                permissionInfo += "Manifest.permission.SEND_SMS Deny \n";
            }

            //调取相机
            if (addPermission(permissions, Manifest.permission.CAMERA)) {
                permissionInfo += "Manifest.permission.CAMERA Deny \n";
            }

            //创建快捷方式
            if (addPermission(permissions, Manifest.permission.INSTALL_SHORTCUT)) {
                permissionInfo += "Manifest.permission.INSTALL_SHORTCUT Deny \n";
            }

            //删除快捷方式
            if (addPermission(permissions, Manifest.permission.UNINSTALL_SHORTCUT)) {
                permissionInfo += "Manifest.permission.UNINSTALL_SHORTCUT Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            } else {
                chackUpdata();
            }
        } else {
            chackUpdata();
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        KLog.d("requestCode", requestCode+"");
        if (requestCode == SDK_PERMISSION_REQUEST) {
            KLog.d("permission", "success");
            chackUpdata();
        }
    }

    @Override
    public void DownSuccessListener(String filePath) {
        KLog.d(filePath);
        FileUtil.openFile(this,filePath);
    }

    @Override
    public void DownDefeatedListener(String failMsg) {
        if(!thread.isAlive()) {
            thread.start();
        }
    }


    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(1000);
                login();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDialog(){
        initDialogView();
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initDialogView() {
        view= LayoutInflater.from(this).inflate(R.layout.dialog_update,null);
        mtvCenter=(TextView)view.findViewById(R.id.tv_center);
        mbtnCancel=(Button)view.findViewById(R.id.btn_dialog_cancel);
        mbtnUpdate=(Button)view.findViewById(R.id.btn_dialog_update);
        mtvCenter.setText(info.getDes());
        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if(!thread.isAlive()) {
                    thread.start();
                }
            }
        });

        mbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                downDialog=new DownLoadDialog(WelcomeActivity.this,info.getUrl(),Environment.getExternalStorageDirectory().getPath(),WelcomeActivity.this);
            }
        });
    }


}
