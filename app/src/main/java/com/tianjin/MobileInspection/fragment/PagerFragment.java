package com.tianjin.MobileInspection.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tianjin.MobileInspection.R;

/**
 * Created by wuchang on 2016/10/25.
 */
public class PagerFragment extends Fragment {

    private Context context;
    private View view;
    private ImageView mivShow;

    private Bitmap path;
    private int showid;

    public PagerFragment(){}

    public static PagerFragment getPagerFragment( Bitmap path){
        return new PagerFragment(path);
    }

    private PagerFragment(Bitmap path){
        this.path=path;
    }

    public int getShowid() {
        return showid;
    }

    public void setShowid(int id) {
        this.showid = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_show_has_chosed,container,false);
        mivShow=(ImageView)view.findViewById(R.id.img_show_chosed);
        if(path!=null){
            mivShow.setImageBitmap(path);
        }
        return view;
    }


}
