package com.tianjin.MobileInspection.activity.userinfo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by wuchang on 2016-12-2.
 * <p>
 * 设置
 */
public class UserInfoActivity extends BaseActivity {

    private static final int INT_REQUESTCODE = 5236;
    private static final int INT_IMAGE_CUT = 5237;
    private static final int INT_IMAGEUPLOAD = 5238;
    private static final int INT_USERINFO_REQUESTCODE = 5239;

    public static final String STR_CHANG_TYPE = "type";
    public static final String STR_CHANG_NAME = "name";

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private LinearLayout mlinearName;
    private LinearLayout mlinearPhoto;
    private LinearLayout mlinearEmail;
    private LinearLayout mlinearAddress;
    private LinearLayout mlinearHead;

    private TextView mtvName;
    private TextView mtvPhone;
    private TextView mtvEmail;
    private TextView mtvAddress;

    private CircleImg mciHead;

    private Bitmap bitmapCut;
    private String filename;

    private LinearLayout mlinearProcessChose;
    private Switch mstProcess;
    private CheckBox mcbdenoise;
    private CheckBox mcbvacuate;
    private CheckBox mcbmapmatch;

    private boolean process = MyApplication.getBooleanSharedPreferences("process", true);
    private boolean denoise = MyApplication.getBooleanSharedPreferences("denoise", true);
    private boolean vacuate = MyApplication.getBooleanSharedPreferences("vacuate", true);
    private boolean mapmatch = MyApplication.getBooleanSharedPreferences("mapmatch", false);

    private RelativeLayout mrelatLightMAX;
    private TextView mtvMaxNUM;

    private Dialog dialog;
    private EditText medtMax;
    private Button mbtnCancel;
    private Button mbtnSure;

    private CompoundButton.OnCheckedChangeListener swchangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                process=true;
                mlinearProcessChose.setVisibility(View.VISIBLE);
                MyApplication.setIntegerSharedPreferences("isProcess", 1);
            } else {
                process=false;
                mlinearProcessChose.setVisibility(View.GONE);
                MyApplication.setIntegerSharedPreferences("isProcess", 0);
            }
            MyApplication.setBooleanSharedPreferences("process", process);
        }
    };

    private CompoundButton.OnCheckedChangeListener cbchangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cb_denoise:
                    if (isChecked) {
                        denoise = true;
                    } else {
                        denoise = false;
                    }
                    MyApplication.setBooleanSharedPreferences("denoise", denoise);
                    break;
                case R.id.cb_vacuate:
                    if (isChecked) {
                        vacuate = true;
                    } else {
                        vacuate = false;
                    }
                    MyApplication.setBooleanSharedPreferences("vacuate", vacuate);
                    break;
                case R.id.cb_mapmatch:
                    if (isChecked) {
                        mapmatch=true;
                    } else {
                        mapmatch=false;
                    }
                    MyApplication.setBooleanSharedPreferences("mapmatch", mapmatch);
                    break;
            }
            setProcessOption();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initData();
    }

    private void initData() {
        mtvTitle.setText("设置");
        String headname = MyApplication.getStringSharedPreferences(MyApplication.USER_INFO_PHOTO_NAME, "a");
        File file = new File(MyApplication.IMAGEFILEPATH, headname);
        KLog.d(file.getPath());
        mciHead.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
        mtvName.setText(MyApplication.getStringSharedPreferences("name", ""));
        mtvPhone.setText(MyApplication.getStringSharedPreferences(MyApplication.USER_INFO_PHONE, ""));
        mtvEmail.setText(MyApplication.getStringSharedPreferences(MyApplication.USER_INFO_EMAIL, ""));

        mstProcess.setChecked(process);
        mcbdenoise.setChecked(denoise);
        mcbvacuate.setChecked(vacuate);
        mcbmapmatch.setChecked(mapmatch);

        int max=MyApplication.getIntSharedPreferences("lightMaxNum",0);
        mtvMaxNUM.setText(max+"");
    }

    private void initView() {
        mlinearBack = (LinearLayout) findViewById(R.id.linear_return_back);
        mtvTitle = (TextView) findViewById(R.id.tv_activity_title);

        mlinearName = (LinearLayout) findViewById(R.id.linear_user_name);
        mlinearPhoto = (LinearLayout) findViewById(R.id.linear_user_phone);
        mlinearEmail = (LinearLayout) findViewById(R.id.linear_user_email);
        mlinearAddress = (LinearLayout) findViewById(R.id.linear_user_address);
        mlinearHead = (LinearLayout) findViewById(R.id.linear_user_head);

        mtvName = (TextView) findViewById(R.id.tv_user_name);
        mtvPhone = (TextView) findViewById(R.id.tv_user_phone);
        mtvEmail = (TextView) findViewById(R.id.tv_user_email);
        mtvAddress = (TextView) findViewById(R.id.tv_user_address);

        mciHead = (CircleImg) findViewById(R.id.ci_user_head);

        mlinearProcessChose = (LinearLayout) findViewById(R.id.linear_map_process_chose);
        mstProcess = (Switch) findViewById(R.id.switch_open_process);
        mcbdenoise = (CheckBox) findViewById(R.id.cb_denoise);
        mcbvacuate = (CheckBox) findViewById(R.id.cb_vacuate);
        mcbmapmatch = (CheckBox) findViewById(R.id.cb_mapmatch);

        mrelatLightMAX=(RelativeLayout)findViewById(R.id.relat_light_max_num);
        mtvMaxNUM=(TextView)findViewById(R.id.tv_light_max_num);

        mlinearBack.setOnClickListener(this);
        mlinearPhoto.setOnClickListener(this);
        mlinearEmail.setOnClickListener(this);
        mlinearAddress.setOnClickListener(this);
        mlinearHead.setOnClickListener(this);
        mrelatLightMAX.setOnClickListener(this);

        mstProcess.setOnCheckedChangeListener(swchangeListener);

        mcbdenoise.setOnCheckedChangeListener(cbchangeListener);
        mcbvacuate.setOnCheckedChangeListener(cbchangeListener);
        mcbmapmatch.setOnCheckedChangeListener(cbchangeListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_return_back:
                setResult(RESULT_OK, getIntent());
                this.finish();
                break;
            case R.id.linear_user_phone:
                toeditInfoActivity("android.intent.action.USERINFOEDITACTIVITY", 2, "电话");
                break;
            case R.id.linear_user_email:
                toeditInfoActivity("android.intent.action.USERINFOEDITACTIVITY", 3, "邮箱");
                break;
            case R.id.linear_user_address:
                toeditInfoActivity("android.intent.action.USERINFOEDITACTIVITY", 4, "地址");
                break;
            case R.id.linear_user_head:
                toeditInfoActivity("android.intent.action.MULTIIMAGESELECTORACTIVITY", 0, null);
                break;
            case R.id.relat_light_max_num:
                if(dialog==null){
                    initDialog();
                }else {
                    dialog.show();
                }
                break;
        }
    }

    private void initDialog() {
        View v= LayoutInflater.from(this).inflate(R.layout.dialog_light_num,null);
        medtMax=(EditText)v.findViewById(R.id.edt_max);
        mbtnCancel=(Button)v.findViewById(R.id.btn_cancel);
        mbtnSure=(Button)v.findViewById(R.id.btn_sure);
        dialog=new Dialog(this);
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mbtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str= CheckUntil.checkEditext(medtMax.getText());
                if(str.equals("")){
                    Toast.makeText(UserInfoActivity.this,"请填写具体的数字",Toast.LENGTH_SHORT).show();
                }else {
                    int max=Integer.valueOf(str);
                    mtvMaxNUM.setText(str);
                    MyApplication.setIntegerSharedPreferences("lightMaxNum",max);
                    dialog.cancel();
                }
            }
        });

        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void toeditInfoActivity(String action, int type, String name) {
        Intent intent = new Intent(action);
        if (type == 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
            startActivityForResult(intent, INT_REQUESTCODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(STR_CHANG_TYPE, type);
            bundle.putString(STR_CHANG_NAME, name);
            intent.putExtras(bundle);
            startActivityForResult(intent, INT_USERINFO_REQUESTCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INT_REQUESTCODE && data != null) {
                KLog.d(data.getExtras().toString());
                List<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (list != null || list.size() == 0) {
                    KLog.d(list.get(0).toString());
                    imageCut(Uri.fromFile(new File(list.get(0))));
                    filename = FileUtil.getFileNameForUrl(list.get(0));
                    KLog.d(filename);
                }
            } else if (requestCode == INT_IMAGE_CUT && data != null) {
                Bundle bundle = data.getExtras();
                bitmapCut = bundle.getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //compress方法是把一个位图写到一个OutputStream中,参数一是位图对象，二是格式,三是压缩的质量，四是输出流
                bitmapCut.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                //输出流转成输入流
                final InputStream inputimage = new ByteArrayInputStream(baos.toByteArray());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = INT_IMAGEUPLOAD;
                        msg.obj = inputimage;
                        handler.sendMessage(msg);
                    }
                }).start();
//                final BufferedInputStream inputImage = new BufferedInputStream(inputimage);
                mciHead.setImageBitmap(bitmapCut);
            } else if (requestCode == INT_USERINFO_REQUESTCODE && data != null) {
                Bundle bundle = data.getExtras();
                switch (bundle.getInt(STR_CHANG_TYPE)) {
                    case 2:
                        mtvPhone.setText(bundle.getString(STR_CHANG_NAME));
                        break;
                    case 3:
                        mtvEmail.setText(bundle.getString(STR_CHANG_NAME));
                        break;
                    case 4:
                        mtvAddress.setText(bundle.getString(STR_CHANG_NAME));
                        break;
                }
            }

        }
    }

    private void imageCut(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);// 输出图片大小
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, INT_IMAGE_CUT);
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what) {
            case INT_IMAGEUPLOAD:
                try {
                    FileUploadModule.postFile(this, handler, (InputStream) msg.obj, filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case FileUploadModule.INT_UPLOAD_SUCCESS:
                Toast.makeText(this, "上传头像成功", Toast.LENGTH_SHORT).show();
                break;
            case FileUploadModule.INT_UPLOAD_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK, getIntent());
        this.finish();
        return super.onKeyDown(keyCode, event);
    }


    public void setProcessOption(){
        StringBuffer sb=new StringBuffer();

        if (denoise){
            sb.append("need_denoise=1");
        }else {
            sb.append("need_denoise=0");
        }

        if(vacuate){
            sb.append(",need_vacuate=1");
        }else {
            sb.append(",need_vacuate=0");
        }

        if(mapmatch){
            sb.append(",need_mapmatch=1");
        }else {
            sb.append(",need_mapmatch=0");
        }

        MyApplication.setStringSharedPreferences("processOption",sb.toString());
    }
}
