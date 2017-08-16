package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.ImageItem;

import java.util.List;

/**
 * Created by wuchang on 2016-12-12.
 */
public class ChoseImageAdapter extends BaseAdapter{

    private Context context;
    private List<ImageItem> data;

    public ChoseImageAdapter(Context context){
        this.context=context;
    }

    public void updata(List<ImageItem> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Hodler hodler=null;
        if(convertView==null){
            hodler=new Hodler();
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_chose_image_item,null);
            hodler.chose=(me.nereo.multi_image_selector.view.SquaredImageView)convertView.findViewById(R.id.iv_image_chosed);
            convertView.setTag(hodler);
        }else {
            hodler=(Hodler)convertView.getTag();
        }
        ImageItem ii = data.get(position);
        KLog.d(position+"",ii.getImagePath());
        Bitmap bitmap=ii.getBitmap();
        if(bitmap!=null) {
            hodler.chose.setImageBitmap(ii.getBitmap());
        }else {
            hodler.chose.setImageResource(R.mipmap.image_default_error);
        }
        return convertView;
    }

    private class Hodler{
        me.nereo.multi_image_selector.view.SquaredImageView chose;
    }
}
