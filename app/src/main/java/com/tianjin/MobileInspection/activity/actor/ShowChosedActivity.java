package com.tianjin.MobileInspection.activity.actor;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;

import java.util.ArrayList;

/**
 * Created by wuchang on 2016/10/24.
 *
 * 展示选择的图片
 */
public class ShowChosedActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mlinearBack;
    private Button mbtnDelete;
    private TextView mtvShowInfo;
    private ViewPager mvpShowData;
    private Intent intent;
    private int mintShowID;
    private ArrayList<String> showData;//预览图片的路径列表
    private boolean show;

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
        intent = getIntent();
        showData = intent.getStringArrayListExtra("data");
        mintShowID = intent.getExtras().getInt("id");
        KLog.d(mintShowID + "   " + showData.size());
        show = intent.getBooleanExtra("show", false);
        if (show) {
            mbtnDelete.setVisibility(View.GONE);
        } else {
            mbtnDelete.setVisibility(View.VISIBLE);
        }
        list=new ArrayList<ImageView>();
        adapter=new MyViewAdapter();
        mvpShowData.setAdapter(adapter);
        for(int i=0;i<showData.size();i++) {
            ImageView iv=new ImageView(this);
            if(showData.get(i)==null||showData.get(i).equals("")){
                iv.setImageResource(R.mipmap.image_non_existent_1);
            }else {
                iv.setImageBitmap(BitmapFactory.decodeFile(showData.get(i)));
            }
            list.add(iv);
        }

        adapter.upData(list);
        mvpShowData.setCurrentItem(mintShowID);

        mvpShowData.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mintShowID = position;
                mtvShowInfo.setText((position + 1) + "/" + list.size());

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
        mlinearBack = (LinearLayout) findViewById(R.id.linear_show_back);
        mbtnDelete = (Button) findViewById(R.id.btn_show_finish);
        mtvShowInfo = (TextView) findViewById(R.id.tv_show_info);
        mvpShowData = (ViewPager) findViewById(R.id.vp_show_data);
        mlinearBack.setOnClickListener(this);
        mbtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_show_back:
                showFinished();
                break;
            case R.id.btn_show_finish:
                KLog.d(mintShowID+"");
                showData.remove(mintShowID);
                //当展示的图片被删除完之后，预览界面退出
                if (showData.size() == 0) {
                    showFinished();
                } else {
                    list.remove(mintShowID);
                    if (mintShowID == showData.size()) {
                        mintShowID--;
                    }
                    adapter.upData(list);
                    mvpShowData.setCurrentItem(mintShowID);
                    mtvShowInfo.setText((mintShowID + 1) + "/" + list.size());
                }
                break;
        }
    }

    private void showFinished() {
        intent.putStringArrayListExtra("data", showData);
        setResult(RESULT_OK, intent);
        ShowChosedActivity.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showFinished();
        return true;
    }


    class MyViewAdapter extends PagerAdapter{

        private ArrayList<ImageView> mviewData;


        public void upData(ArrayList<ImageView> mviewData){
            this.mviewData=mviewData;
            notifyDataSetChanged();
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}

