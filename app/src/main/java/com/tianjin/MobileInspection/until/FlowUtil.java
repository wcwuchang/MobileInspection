package com.tianjin.MobileInspection.until;

/**
 * Created by 吴昶 on 2016/12/15.
 *
 * 工作流的状态标记
 */
public class FlowUtil {

    /**
     * 流程状态
     * @param statue
     * @return
     */
    public static int flowStatus(String statue){
        if(statue.equals("claim")){//待签收
            return -1;
        }else if(statue.equals("todo")){//待办
            return 0;
        }else if(statue.equals("finish")){//完成
            return 1;
        }
        return 1;
    }

    /**
     * 交通类型
     * @param id
     * @return
     */
    public static String traffic(String id){
        if(id.equals("1")){
            return "步行";
        }else if(id.equals("2")){
            return "开车";
        }
        return id;
    }

    /**
     * 待办事项分类
     * @param proc
     * @return
     */
    public static int todotype(String proc){
        if(proc.equals("inspecting_approval")){//巡检
            return 1;
        }else if(proc.equals("accident_approval")){//隐患
            return 2;
        }else if(proc.equals("daily_maintenance_approval")){//日常维修（废弃）
            return 3;
        }else if(proc.equals("special_maintenance_approval")){//专项（废弃）
            return 4;
        }else if(proc.equals("maneuver_maintenance")){//机动维修
            return 5;
        }else if(proc.equals("plan_maintenance_approval")){//计划维修
            return 6;
        }
        return 7;
    }

    /**
     * 日常专项流程状态
     * @param str
     * @return
     */
    public static int deilaytype(String str){
        if(str.equals("daily_start_maintenance")||str.equals("special_start_maintenance")){
            return 1;//开始维修
        }else if(str.equals("daily_check_maintenancer")||str.equals("special_check_maintenancer")){
            return 2;//选择维修人
        }else if(str.equals("daily_report")||str.equals("special_report")){
            return 3;//上报
        }else if(str.equals("daily_metering_review")||str.equals("special_metering_review")){
            return 4;//计量
        }
        return -1;
    }

    /**
     * 巡检任务的流程状态
     * @param state
     * @return
     */
    public static String inspectionState(int state){
        switch (state){
            case 1:
                return "待巡检";
            case 2:
                return "巡检中";
            case 3:
                return "巡检结束";
            case 4:
                return "验收结束";
            default:
                return "待巡检";
        }
    }


    /**
     * 待办流程节点
     * @param codeId
     * @return
     */
    public static int todoFlowCode(String codeId){
        switch (codeId){
            case "start"://开始（不加入判断）
                return 1;
            case "make_man"://专业工程师下发任务（不加入判断）
                return 2;
            case "program_man"://签收（维修人员）
                return 3;
            case "program_report"://方案上报（维修人员）
                return 4;
            case "program_approval_first"://专业工程师审批（方案）
                return 5;
            case "program_approval_one"://以及审批（方案）
                return 6;
            case "maintenance_report"://维修上报（维修）
                return 7;
            case "approval_first"://专业工程师审批（维修）
                return 8;
            case "approval_one"://一级审批（维修）
                return 9;
        }
        return 0;
    }

}
