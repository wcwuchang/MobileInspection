package com.tianjin.MobileInspection.activity.actor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;

import java.util.ArrayList;

/**
 * Created by wuchang on 2016/10/24.
 *
 * 展示网络返回的图片
 */
public class ShowImageIntnetdActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout mlinearBack;
    private Button mbtnDelete;
    private TextView mtvShowInfo;
    private ViewPager mvpShowData;
    private Intent intent;
    private int mintShowID;
    private ArrayList<String> showData;//预览图片的路径列表
    private MyViewAdapter adapter;
    private ArrayList<ImageView> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chosed);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        showData=intent.getStringArrayListExtra("data");
        mintShowID=intent.getExtras().getInt("id");
        mbtnDelete.setVisibility(View.GONE);
        list=new ArrayList<ImageView>();
        if(showData!=null&&showData.size()>0){
            for(int i=0;i<showData.size();i++){
                ImageView iv=new ImageView(this);
                if(showData.get(i)==null||showData.get(i).equals("")){
                    iv.setImageResource(R.mipmap.image_non_existent_1);
                }else {
                    iv.setImageBitmap((BitmapFactory.decodeFile(showData.get(i))));
                }
                list.add(iv);
            }
        }
        adapter=new MyViewAdapter(list);
        mvpShowData.setAdapter(adapter);
        mvpShowData.setCurrentItem(mintShowID);

        mvpShowData.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mintShowID=position;
                mtvShowInfo.setText((position+1)+"/"+list.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mlinearBack=(LinearLayout) findViewById(R.id.linear_show_back);
        mbtnDelete=(Button)findViewById(R.id.btn_show_finish);
        mtvShowInfo=(TextView)findViewById(R.id.tv_show_info);
        mvpShowData=(ViewPager)findViewById(R.id.vp_show_data);
        mlinearBack.setOnClickListener(this);
        mbtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_show_back:
                finish();
                break;
        }
    }


    class MyViewAdapter extends PagerAdapter{

        private ArrayList<ImageView> mviewData;

        public MyViewAdapter (ArrayList<ImageView> mviewData){
            this.mviewData=mviewData;
        }

        @Override
        public int getCount() {
            return mviewData==null?0:mviewData.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mviewData.get(position),0);
            return mviewData.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
}

