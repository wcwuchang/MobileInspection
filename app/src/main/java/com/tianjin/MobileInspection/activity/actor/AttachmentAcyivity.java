package com.tianjin.MobileInspection.activity.actor;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by 吴昶 on 2016/12/25.
 *
 * 附件上传
 * 未使用此activity
 */
public class AttachmentAcyivity extends BaseActivity {

    private static final int INT_IMAGE_CHOSE=0x5238;
    private static final int INT_FILE_CHOSE=0x5239;

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private TextView mtvSave;

    private Button mbtnAdd;

    private Dialog choseDialog;
    private View dialogView;
    private TextView mtvImage;
    private TextView mtvFile;
    private TextView mtvCancel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fujian_upload);
        initView();
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSave=(TextView)findViewById(R.id.tv_activity_mun);

        mbtnAdd=(Button)findViewById(R.id.btn_fujian_add);

        mlinearSave.setVisibility(View.VISIBLE);
        mtvTitle.setText("附件上传");
        mtvSave.setText("完成");
        mbtnAdd.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_fujian_add:
                if(choseDialog==null) {
                    choseDialoginit();
                }else {
                    choseDialog.show();
                }
                break;
            case R.id.linear_save:
            case R.id.linear_return_back:
                finish();
                break;
        }
    }

    private void choseDialoginit() {
        initDialogView();
        choseDialog=new Dialog(this);
        choseDialog.setContentView(dialogView);
        choseDialog.setCanceledOnTouchOutside(false);
        choseDialog.show();
    }

    private void initDialogView() {
        dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_fujian_add,null);
        mtvImage=(TextView)dialogView.findViewById(R.id.tv_fujian_add_img);
        mtvFile=(TextView)dialogView.findViewById(R.id.tv_fujian_add_file);
        mtvCancel=(TextView)dialogView.findViewById(R.id.tv_fujian_add_cancel);
        mtvImage.setOnClickListener(dialogListener);
        mtvFile.setOnClickListener(dialogListener);
        mtvCancel.setOnClickListener(dialogListener);
    }

    View.OnClickListener dialogListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_fujian_add_img:
                    Intent intent=new Intent("android.intent.action.MULTIIMAGESELECTORACTIVITY");
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,1);
                    startActivityForResult(intent,INT_IMAGE_CHOSE);
                    break;
                case R.id.tv_fujian_add_file:
                    Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent1.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent1,INT_FILE_CHOSE);
                    break;
                case R.id.tv_fujian_add_cancel:
                    break;
            }
            choseDialog.cancel();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&data!=null){
            if(requestCode==INT_IMAGE_CHOSE){
                List<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if(list!=null||list.size()==0) {
                    KLog.d(list.get(0).toString());
                }
            }else if(requestCode==INT_FILE_CHOSE){
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor.getString(actual_image_column_index);
//                File file = new File(img_path);
                KLog.d(img_path);
            }
        }
    }
}
