package com.tianjin.MobileInspection.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.module.HiddenModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by 吴昶 on 2017/2/28.
 */
public class HiddenTimeFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private PieChart pieChart;
    private LineChartView lineChart;

    private HiddenModule module;
    private List<PlanManage> list;
    private int index = 1;
    private int maxY;
    public void initFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hidden_time, container, false);
            initView();
            initData();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        list = new ArrayList<PlanManage>();
        module = new HiddenModule(handler, context);
        module.getTroubles("1", "0");
    }

    private void initView() {
        pieChart = (PieChart) view.findViewById(R.id.pc_hidden_time);
        lineChart = (LineChartView) view.findViewById(R.id.lc_chat);
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what) {
            case HiddenModule.INT_GET_TROUBLES_LIST_SUCCESS:
                list.addAll((ArrayList<PlanManage>) msg.obj);
                if (index != 2) {
                    index++;
                    module.getTroubles("1", "1");
                } else {
                    try {
                        drawLine();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case HiddenModule.INT_GET_TROUBLES_LIST_Filed:
                break;
        }
    }

    private String[] x;
    private int[] y;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    private void drawLine() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < list.size(); i++) {
            Date data = sdf.parse(list.get(i).getPlanEndDate());
            list.get(i).setPlanEndDate(sdf2.format(data));
        }

        ArrayList<Integer> cishu = new ArrayList<Integer>();
        ArrayList<String> months = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            boolean has = false;
            for (int j = 0; j < months.size(); j++) {
                if (list.get(i).getPlanEndDate().equals(months.get(j))) {
                    int g = cishu.get(j) + 1;
                    cishu.remove(j);
                    cishu.add(j, g);
                    has = true;
                    break;
                }
            }
            if (has) continue;
            cishu.add(1);
            months.add(list.get(i).getPlanEndDate());
        }
//        x = new String[months.size()];
//        y = new int[months.size()];
//        for (int i = 0; i < months.size(); i++) {
//            x[i] = months.get(months.size()-i-1);
//            y[i] = cishu.get(cishu.size()-i-1);
//        }

        x = new String[]{"2016-08","2016-09","2016-10","2016-11","2016-12","2017-01","2017-02","2017-03"};
        y = new int[]{13,28,4,9,15,17,20,9};
        maxY=28+5;
        drawLinearChat();
    }

    private void drawLinearChat() {
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < x.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(x[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < y.length; i++) {
            mPointValues.add(new PointValue(i, y[i]));
        }
}

    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //
        List<Line> lines = new ArrayList<Line>();
        //折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setShape(ValueShape.CIRCLE);
        //曲线是否平滑，即是曲线还是折线
        line.setCubic(false);
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

        List<PointValue> jin=new ArrayList<PointValue>();
        for(int i=0;i<mPointValues.size();i++){
            jin.add(new PointValue(i,26));
        }
        Line jingao=new Line(jin).setColor(Color.RED);
        jingao.setHasPoints(false);
        jingao.hasLabels();
        lines.add(jingao);
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("月份");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        axisX.setHasSeparationLine(true);
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("月上报次数");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        axisY.setHasLines(true);

        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        data.setValueLabelBackgroundAuto(true);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        lineChart.setViewportCalculationEnabled(true);
//        lineChart.setBackgroundColor(Color.rgb(97,97,97));
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.bottom = 0;//y轴最小值
        if(maxY<=26){
            v.top = 27;//y轴最大值
        }else {
            v.top = maxY;
        }
        v.left = 0;//x轴最小刻度值
//        v.right = 7;//x轴展示最大刻度值
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
    }


        @Override
    public void onClick(View v) {

    }
}
