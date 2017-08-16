package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.NewInspectionChose;
import com.tianjin.MobileInspection.entity.Office;
import com.tianjin.MobileInspection.entity.Road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-9.
 */
public class NewInspectionChoseModule {

    private Handler activity;
    private List<NewInspectionChose> data=new ArrayList<NewInspectionChose>();
    private Context context;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ContractModule.INT_CONTRACT_LIST_SUCCESS:
                    List<ContractManager> list=(List<ContractManager>)msg.obj;
                    data.clear();
                    if(list!=null&&list.size()>0) {
                        for (ContractManager contract : list) {
                            NewInspectionChose nic = new NewInspectionChose();
                            nic.setId(contract.getContractId());
                            nic.setName(contract.getName());
                            data.add(nic);
                        }
                    }
                    datatoactivity(ContractModule.INT_CONTRACT_LIST_SUCCESS);
                    break;
                case InspectionModule.INT_GET_ROAD_SUCCESS:
                    List<Road> roads=(List<Road>)msg.obj;
                    data.clear();
                    if(roads!=null&&roads.size()>0) {
                        for (Road road : roads) {
                            NewInspectionChose nic = new NewInspectionChose();
                            nic.setId(road.getRoadId());
                            nic.setName(road.getRoadName());
                            data.add(nic);
                        }
                    }
                    datatoactivity(InspectionModule.INT_GET_ROAD_SUCCESS);
                    break;
                case InspectionModule.INT_GET_OFFICE_LIST_SUCCESS:
                    List<Office> offices=(List<Office>)msg.obj;
                    data.clear();
                    if(offices!=null&&offices.size()>0) {
                        for (Office off : offices) {
                            NewInspectionChose nic = new NewInspectionChose();
                            nic.setId(off.getOfficeId());
                            nic.setName(off.getOfficeName());
                            data.add(nic);
                        }
                    }
                    datatoactivity(InspectionModule.INT_GET_OFFICE_LIST_SUCCESS);
                    break;
                case InspectionModule.INT_GET_TROUBLES_LIST_SUCCESS:
                    List<HiddenTroubleDetail> details=(List<HiddenTroubleDetail>)msg.obj;
                    data.clear();
                    if(details!=null&&details.size()>0) {
                        for (HiddenTroubleDetail detail : details) {
                            NewInspectionChose nic = new NewInspectionChose();
                            nic.setId(detail.getTroubleId());
                            nic.setName(detail.getTitle());
                            Map<String,Object> map=new HashMap<String,Object>();
                            map.put("longtitude",detail.getLongitude());
                            map.put("latitude",detail.getLatitude());
                            map.put("name",detail.getInspectionName());
                            map.put("data", detail.getDate());
                            map.put("content",detail.getContent());
                            nic.setMap(map);
                            data.add(nic);
                        }
                    }
                    datatoactivity(InspectionModule.INT_GET_TROUBLES_LIST_SUCCESS);
                    break;

                case InspectionModule.INT_GET_OFFICE_LIST_FILED:
                case InspectionModule.INT_GET_ROAD_FILED:
                case ContactsModule.INT_GET_CONTACTS_FILEAD:
                case InspectionModule.INT_GET_TROUBLES_LIST_Filed:
                    activity.sendMessage(msg);
                    break;
            }

        }
    };

    private void datatoactivity(int what){
        Message msg=new Message();
        msg.what=what;
        msg.obj=data;
        activity.sendMessage(msg);
    }

    public NewInspectionChoseModule(Handler activity){
        this.activity=activity;
    }

    //获取合同列表
    public void getContracts(Context context,final int pageno, final int pagesize){
        ContractModule cm=new ContractModule(handler,context);
        cm.getContractList(pageno,pagesize);
    }

    //获取道路列表
    public void getRoads(Context context,final int pageno, final int pagesize){
        InspectionModule im=new InspectionModule(handler,context);
        im.getRoads(pageno,pagesize);
    }

    //获取单位列表
    public void getOffice(Context context){
        InspectionModule im=new InspectionModule(handler,context);
        im.getOfficeList();
    }

    //获取隐患列表
    public void getTroubles(Context context,final int pageno, final int pagesize){
        InspectionModule im=new InspectionModule(handler,context);
        im.getTroubles(String.valueOf(pageno),String.valueOf(pagesize));
    }

}
