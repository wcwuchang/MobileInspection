package com.tianjin.MobileInspection.activity.inspection;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by wuchang on 2016-12-8.
 *
 * 新建巡检计划
 */
public class NewInspectionPlanActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private LinearLayout mlinearAdd;
    private ImageView mivAdd;
    private TextView mtvTitle;

    private EditText medtInspectionName;
    private EditText medtInspectionContent;
    private RelativeLayout mrelatSrart;
    private RelativeLayout mrelatEnd;
    private TextView mtvStart;
    private TextView mtvEnd;
    final Calendar c = Calendar.getInstance();
    private AlertDialog.Builder tuichuDialog;
    private String selectedDate;
    private String starttime="";
    private String endtime="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inspection_plan);
        initView();
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearAdd=(LinearLayout)findViewById(R.id.linear_add);
        mivAdd=(ImageView)findViewById(R.id.iv_add);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        medtInspectionName=(EditText)findViewById(R.id.edt_new_inspection_plan_name);
        medtInspectionContent=(EditText)findViewById(R.id.edt_new_inspection_plan_content);
        mrelatSrart=(RelativeLayout)findViewById(R.id.relat_new_inspection_start);
        mrelatEnd=(RelativeLayout)findViewById(R.id.relat_new_inspection_plan_end);
        mtvStart=(TextView)findViewById(R.id.tv_new_inspection_plan_start);
        mtvEnd=(TextView)findViewById(R.id.tv_new_inspection_plan_end);

        mlinearAdd.setVisibility(View.VISIBLE);
        mtvTitle.setText("新建巡检计划");

        mlinearBack.setOnClickListener(this);
        mlinearAdd.setOnClickListener(this);
        mrelatSrart.setOnClickListener(this);
        mrelatEnd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.relat_new_inspection_start:
                date(mtvStart,true);
                break;
            case R.id.relat_new_inspection_plan_end:
                date(mtvEnd,false);
                break;
            case R.id.linear_return_back:
                finish();
                break;
        }

    }

    private void date(final TextView v,final boolean isStart){
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                // Calendar月份是从0开始,所以month要加1
                try {
                    java.util.Date currentdate = new java.util.Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                    selectedDate = year + "-" + (month + 1) + "-" + (dayOfMonth + 1);
                    java.util.Date d;
                    d = sdf.parse(selectedDate);
                    if (currentdate.before(d)) {
                        if(isStart){
                            if(!endtime.equals("")){
                                java.util.Date end=sdf.parse(endtime);
                                if(end.before(d)){
                                    mtvEnd.setText("");
                                    endtime="";
                                }
                            }
                            starttime=year + "-" + (month + 1) + "-" + dayOfMonth;
                            v.setText(starttime);
                        }else{
                            if(!starttime.equals("")){
                                java.util.Date start=sdf.parse(starttime);
                                if(d.before(start)){
                                    datebijiao("截止日期不能早于开始日期");
                                    return;
                                }

                            }
                            endtime=year + "-" + (month + 1) + "-" + dayOfMonth;
                            v.setText(endtime);
                        }

                    } else {
                        if(isStart) {
                            datebijiao("计划的开始日期不能早于当前日期");
                        }else{
                            datebijiao("计划的截止日期不能早于当前日期");
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };
        DatePickerDialog dialog = new DatePickerDialog(this, dateListener, c
                .get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void datebijiao(String msg){
        tuichuDialog = new AlertDialog.Builder(NewInspectionPlanActivity.this)
                .setMessage(msg);
        tuichuDialog.setPositiveButton("知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        tuichuDialog.show();
    }
}
