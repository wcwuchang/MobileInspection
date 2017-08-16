package com.tianjin.MobileInspection.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;

/**
 * Created by wuchang on 2016-12-27.
 */
public class ConfigureActivity extends BaseActivity{

    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        spinner=(Spinner)findViewById(R.id.sp_connection);
        final String[] connection={"http://139.196.223.73:20102/jeeplus","http://l545665786.vicp.io/jeeplus","http://192.168.1.101:8005/jeeplus"};
        final String[] name={"服务器","公司","家"};
        final String[] image={"http://139.196.223.73:20102/","http://l545665786.vicp.io","http://192.168.1.101:8005"};
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.connection,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        for(int i=0;i<name.length;i++){
            if(MyApplication.getStringSharedPreferences("connection","").equals(connection[i])){
                spinner.setSelection(i);
                break;
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  KLog.d(connection[position]);
                  MyApplication.setStringSharedPreferences("connection",connection[position]);
                  MyApplication.setStringSharedPreferences("image",image[position]);
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
              }
        );

    }
}
