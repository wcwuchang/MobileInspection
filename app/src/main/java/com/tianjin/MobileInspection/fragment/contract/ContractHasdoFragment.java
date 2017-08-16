package com.tianjin.MobileInspection.fragment.contract;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.contract.ContractDetailActivity;
import com.tianjin.MobileInspection.adapter.HiddenTroublesAddAdapter;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by 吴昶 on 2017/2/27.
 */
public class ContractHasdoFragment extends BaseFragment {

    private View view;
    private ListView mlvHasdo;
    private TextView mtvDonull;
    private ContractDetailActivity contractDetail;
    private ContractManager contractManager;
    private HiddenTroublesAddAdapter addadapter;

    private BarChart barChart;
    private XAxis xAxis;
    private ArrayList<HiddenType> hiddens;

    public void init(Context context,ContractDetailActivity contractDetail){
        this.context=context;
        this.contractDetail=contractDetail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_contract_has_do, container, false);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        addadapter=new HiddenTroublesAddAdapter(context);
        mlvHasdo.setAdapter(addadapter);
        contractManager=contractDetail.getCm();
        if(contractManager==null||contractManager.getHiddenTypes().size()==0){
            mtvDonull.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
            return;
        }
        hiddens=contractManager.getHiddenTypes();
        for (int i=0;i<hiddens.size();i++){
            hiddens.get(i).setTroubleContent(String.valueOf(hiddens.get(i).getNum()));
        }
        addadapter.updata(hiddens);

        drawBarChar();

    }

    private void drawBarChar() {
        //1、基本设置
        xAxis=barChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setTouchEnabled(true); // 设置是否可以触摸
        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        //2、y轴和比例尺
        barChart.setDescription("已做工作量");// 数据描述
        barChart.getAxisLeft().setDrawAxisLine(true);//y轴显示
        barChart.getAxisLeft().setEnabled(true);//显示y轴数据左侧
        barChart.getAxisRight().setEnabled(false);

        Legend legend = barChart.getLegend();//隐藏比例尺
        legend.setEnabled(false);

        //3、x轴数据,和显示位置
        ArrayList<String> xValues = new ArrayList<String>();
        //4、y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for(int i=0;i<hiddens.size();i++){
            xValues.add(hiddens.get(i).getTypeName());
            //new BarEntry(20, 0)前面代表数据，后面代码柱状图的位置；
            yValues.add(new BarEntry(Float.valueOf(String.valueOf(hiddens.get(i).getNum())), i));
        }

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//数据位于底部


        //5、设置显示的数字为整形
        BarDataSet barDataSet=new BarDataSet(yValues,"");
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                int n = (int) v;
                return n + "";
            }
        });
        //6、设置柱状图的颜色
//        barDataSet.setColors(new int[]{Color.rgb(104, 202, 37), Color.rgb(192, 32, 32),
//                Color.rgb(34, 129, 197), Color.rgb(175, 175, 175)});
        //7、显示，柱状图的宽度和动画效果
        BarData barData = new BarData(xValues, barDataSet);
        barDataSet.setBarSpacePercent(40f);//值越大，柱状图就越宽度越小；
        barChart.animateXY(1000,1000);
        barChart.setData(barData); //


    }

    private void initView() {
        mlvHasdo=(ListView)view.findViewById(R.id.lv_contract_has_do);
        mtvDonull=(TextView)view.findViewById(R.id.tv_do_null);
        barChart=(BarChart)view.findViewById(R.id.bc_contract_has_do);
    }
}
