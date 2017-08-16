package com.tianjin.MobileInspection.until;

/**
 * 服务器接口
 * Created by wuchang on 2016/9/26.
 */
public class ConnectionURL {

//    public final static String HTTP_SERVER_IP = "http://139.196.223.73:8090/jeeplus";
//        public  static String HTTP_SERVER_IP = "http://10.20.1.211:8888/jeeplus";//公司
//    public final static String HTTP_SERVER_IP = "http://192.168.0.107:8080/jeeplus";//家
        public  static String HTTP_SERVER_IP = "http://116.62.222.161:8080/jeeplus";


    //服务器地址
    private static String HTTP_SERVER = HTTP_SERVER_IP+"/a/";
//    public static String HTTP_SERVER = "";

    //图片获取
//    public final static String HTTP_SERVER_IMAGE = "http://139.196.223.73:8090/";//服务器
//    public final static String HTTP_SERVER_IMAGE = "http://192.168.0.107:8080";//家
//    public  static String HTTP_SERVER_IMAGE = "http://10.20.1.211:8888";//公司
    public  static String HTTP_SERVER_IMAGE = "http://116.62.222.161:8080/";

    public final static String HTTP_SERVER_END_Y ="&__ajax=true&mobileLogin=true";
    public final static String HTTP_SERVER_END ="?__ajax=true&mobileLogin=true";

    //登录接口
    public final static String STR_LOGIN_CONNECTION = HTTP_SERVER + "login?__ajax";

    //退出登录
    public final static String STR_LOGIN_OUT = HTTP_SERVER + "logout;JSESSIONID=";

    //个人信息
    public final static String STR_USER_INFO = HTTP_SERVER + "sys/user/infoData;JSESSIONID=";

    //个人信息修改
    public final static String STR_USER_EDIT = HTTP_SERVER + "sys/user/infoEdit;JSESSIONID=";
    //    参数 name=lily&email=117575171@qq.com&phone=025-83191633&mobile=18951655371&remarks=备注信息

    //图片上传
    public final static String STR_IMAGE_UPLOAD = HTTP_SERVER + "sys/user/imageUpload;JSESSIONID=";

    //通讯录
    public final static String STR_GET_CONTACTS = HTTP_SERVER + "sys/user/findAddressBooksList;JSESSIONID=";

    //联系人详情
    public final static String STR_GET_CONTACTS_DETAIL = HTTP_SERVER + "sys/user/findAddressBooksDetail;JSESSIONID=";
    //单位列表借口
    public final static String STR_GET_OFFICE_LIST = HTTP_SERVER + "sys/office/list;JSESSIONID=";
    //合同列表
    public final static String STR_GET_CONTRACTS_LIST = HTTP_SERVER + "contract/contractMain/getContractList;JSESSIONID=";
    //合同详情
    public final static String STR_GET_CONTRACTS_DETAIL = HTTP_SERVER + "contract/contractMain/getContractDetail;JSESSIONID=";
    //获取道路
    public final static String STR_GET_ROADS = HTTP_SERVER + "parameter/road/road/list;JSESSIONID=";
    //巡检列表查询接口
    public final static String STR_GET_INSPECTION_LIST = HTTP_SERVER + "inspecting/inspectingMain/list;JSESSIONID=";
    //巡检选择列表查询接口
    public final static String STR_GET_INSPECTION_TOINSPECTINGLIST = HTTP_SERVER + "inspecting/inspectingMain/toInspectingList;JSESSIONID=";
    //巡检提交申请接口
    public final static String STR_POST_INSPECTION_SAVE = HTTP_SERVER + "inspecting/inspectingMain/save;JSESSIONID=";
    //巡检提交审批接口
    public final static String STR_POST_INSPECTION_AUDIT = HTTP_SERVER + "inspecting/inspectingMain/saveAudit;JSESSIONID=";
    //待处理接口
    public final static String STR_GET_ACT_TASK_TODO = HTTP_SERVER + "act/task/todo;JSESSIONID=";
    //获取表单url
    public final static String STR_GET_FORM_URL = HTTP_SERVER + "act/task/form;JSESSIONID=";
    //工作流程历史接口
    public final static String STR_GET_ACT_TASK_HISTOICFLOW = HTTP_SERVER + "act/task/histoicFlow;JSESSIONID=";
    //获取已办任务
    public final static String STR_GET_ACT_TASK_HISTOIC = HTTP_SERVER + "act/task/historic;JSESSIONID=";
    //签收
    public final static String STR_GET_ACT_TASK_CLAIM = HTTP_SERVER + "act/task/claim;JSESSIONID=";
    //获取隐患类型
    public final static String STR_GET_TROUBLE_TYPES = HTTP_SERVER + "parameter/unit/unit/list;JSESSIONID=";

    //二维码签到
    public final static String STR_ERCODE_SAVE_CLOCK = HTTP_SERVER + "inspecting/inspectingMain/saveClock;JSESSIONID=";

    //巡检审批提交
    public final static String STR_INSPECTION_APPROVE_COMMIT = HTTP_SERVER + "inspecting/inspectingMain/saveAudit;JSESSIONID=";

    //获取巡检详情（判断是否存在巡检中的巡检）/
    public final static String STR_GET_HAS_DOING_INSPECTION = HTTP_SERVER + "inspecting/inspectingMain/getInspectingTask;JSESSIONID=";
    //获取巡检详情
    public final static String STR_GET_INSPECTION_DETAIL = HTTP_SERVER + "inspecting/inspectingMain/form;JSESSIONID=";

    //隐患提交
    public final static String STR_HIDDEN_TROUBLE_COMMIT = HTTP_SERVER + "accident/accidentMain/save;JSESSIONID=";
    //隐患审批\
    public final static String STR_HIDDEN_TROUBLE_SAVEAUDIT = HTTP_SERVER + "accident/accidentMain/saveAudit;JSESSIONID=";
    //隐患图片提交
    public final static String STR_HIDDEN_TROUBLE_IMAGE_COMMIT = HTTP_SERVER + "accident/accidentMain/imageUpload;JSESSIONID=";

    //获取隐患
    public final static String STR_GET_HIDDEN_TROUBLE_LIST = HTTP_SERVER + "accident/accidentMain/list;JSESSIONID=";
    //获取隐患详情
    public final static String STR_GET_HIDDEN_TROUBLE_DETAIL = HTTP_SERVER + "accident/accidentMain/form;JSESSIONID=";
    //新建日常维修
    public final static String STR_SAVE_DEILAY_TASK = HTTP_SERVER + "dailymaintenance/dailyMaintenanceMain/save;JSESSIONID=";
    //保存专项维修接口
    public final static String STR_SAVE_SPECIAL_TASK = HTTP_SERVER + "specialmaintenance/specialMaintenanceMain/save;JSESSIONID=";
    //日常列表
    public final static String STR_GET_DEILAY_TASK_LIST = HTTP_SERVER + "dailymaintenance/dailyMaintenanceMain/list;JSESSIONID=";
    //日常维修附件
    public final static String STR_SAVE_DEILAY_TASK_FUJIAN = HTTP_SERVER + "dailymaintenance/dailyMaintenanceMain/upload;JSESSIONID=";
    //日常详情
    public final static String STR_GET_DEILAY_TASK_DETAIL = HTTP_SERVER + "dailymaintenance/dailyMaintenanceMain/form;JSESSIONID=";
    //日常审批
    public final static String STR_DEILAY_SAVEAUDIT = HTTP_SERVER + "dailymaintenance/dailyMaintenanceMain/saveAudit;JSESSIONID=";
    //日常维修上报保存
    public final static String STR_SAVE_DEILAY_TASK_REPORT = HTTP_SERVER + "dailymaintenancereport/dailyMaintenanceReport/save;JSESSIONID=";
    //日常维修上报图片上传
    public final static String STR_SAVE_DEILAY_TASK_REPORT_IMAGE = HTTP_SERVER + "dailymaintenancereport/dailyMaintenanceReport/imageUpload;JSESSIONID=";
    //日常维修上报详情
    public final static String STR_SAVE_DEILAY_TASK_REPORT_DETAIL = HTTP_SERVER + "dailymaintenancereport/dailyMaintenanceReport/form;JSESSIONID=";
    //日常维修是否上报
    public final static String STR_SAVE_DEILAY_TASK_IS_REPORTL = HTTP_SERVER + "dailymaintenancereport/dailyMaintenanceReport/getReported;JSESSIONID=";
    //上传专项维修附件接口
    public final static String STR_SAVE_SPECIAL_TASK_UPLOAD= HTTP_SERVER + "specialmaintenance/specialMaintenanceMain/upload;JSESSIONID=";
    //获取专项维修列表
    public final static String STR_GET_SPECIAL_TASK_LIST= HTTP_SERVER + "specialmaintenance/specialMaintenanceMain/list;JSESSIONID=";
    //获取专项维修详情
    public final static String STR_GET_SPECIAL_TASK_DETAIL= HTTP_SERVER + "specialmaintenance/specialMaintenanceMain/form;JSESSIONID=";
    //专项维修上报保存
    public final static String STR_SAVE_SPECIAL_TASK_REPORT= HTTP_SERVER + "specialmaintenancereport/specialMaintenanceReport/save;JSESSIONID=";
    //专项维修上报图片上传
    public final static String STR_SAVE_SPECIAL_TASK_IMAGE= HTTP_SERVER + "specialmaintenancereport/specialMaintenanceReport/imageUpload;JSESSIONID=";
    //专项维修上报详情
    public final static String STR_SAVE_SPECIAL_TASK_DETAIL= HTTP_SERVER + "specialmaintenancereport/specialMaintenanceReport/form;JSESSIONID=";
    //专项审批
    public final static String STR_SPECIAL_SAVEAUDIT = HTTP_SERVER + "specialmaintenance/specialMaintenanceMain/saveAudit;JSESSIONID=";
    //专项维修是否上报
    public final static String STR_SAVE_SPECIAL_TASK_IS_REPORTL = HTTP_SERVER + "specialmaintenancereport/specialMaintenanceReport/getReported;JSESSIONID=";

    //日常维修上报详情
    public final static String STR_SAVE_DEILAY_TASK_SAVEAUDIT = HTTP_SERVER + "dailymaintenance/dailyMaintenance/saveAudit;JSESSIONID=";
    //病害库
    public final static String STR_GET_DISEASEDATEBASE = HTTP_SERVER + "parameter/disease/diseaseDatebase/list;JSESSIONID=";
    //专家库
    public final static String STR_GET_EXPERTDATEBASE = HTTP_SERVER + "parameter/expert/expertDatebase/list;JSESSIONID=";

    //获取计划列表
    public final static String STR_PLAN_LIST=HTTP_SERVER+"/plan/planningManagement/list;JSESSIONID=";
    //获取计划详情
    public final static String STR_PLAN_DETAIL=HTTP_SERVER+"/plan/planningManagement/form;JSESSIONID=";
    //计划保存
    public final static String STR_PLAN_SAVE=HTTP_SERVER+"/plan/planningManagement/save;JSESSIONID=";
    //配置数据接口
    public final static String STR_GET_CONGIGURATION=HTTP_SERVER+"parameter/disease/diseaseDatebase/list;JSESSIONID=";
    //配置数据接口
    public final static String STR_GET_EMERGENCY=HTTP_SERVER+"sys/dict/listData;JSESSIONID=";
    //获取道路信息
    public final static String STR_GET_ROADINFO=HTTP_SERVER+"parameter/road/road/form;JSESSIONID=";
    //暂不维修接口
    public final static String STR_GET_NOT_MAINTANENCE=HTTP_SERVER+"accident/accidentMain/updateMaintenance;JSESSIONID=";

    //方案上报
    public final static String STR_REPORT_SHCEME=HTTP_SERVER+"dailymaintenance/dailyMaintenanceMain/programUpload;JSESSIONID=";
    //延缓维修
    public final static String STR_DEFERRED_MAINTENANCE=HTTP_SERVER+"plan/planningManagement/updateDetailPlan;JSESSIONID=";
    //计划列表
    public final static String STR_GET_PLAN_LIST=HTTP_SERVER+"plan/planningManagement/list;JSESSIONID=";
    //计划shangbao
    public final static String STR_ISSUED_PLAN_TASK=HTTP_SERVER+"plan/planningManagement/batchSave;JSESSIONID=";
    //方案审批(可以获取到上报的详情)
    public final static String STR_REPORT_SHCEME_APPROVE=HTTP_SERVER+"dailymaintenance/dailyMaintenanceMain/dailyMaintenanceProgramAudit;JSESSIONID=";
    //维修审批(可以获取到上报的详情)
    public final static String STR_MAINTENANCE_APPROVE_DETAIL=HTTP_SERVER+"dailymaintenance/dailyMaintenanceMain/dailyMaintenanceReportAudit;JSESSIONID=";
    //方案审批
    public final static String STR_MAINTENANCE_APPROVE=HTTP_SERVER+"dailymaintenance/dailyMaintenanceMain/saveProgramAudit;JSESSIONID=";
    //计量saveReportAudit
    public final static String STR_MAINTENANCE_MAIN_APPROVE=HTTP_SERVER+"dailymaintenance/dailyMaintenanceMain/saveReportAudit;JSESSIONID=";

    //更新
    public final static String STR_UPDATE="http://116.62.222.161:8080/jeeplus/static/upload/update.xml";


    public final static String STR_WEATHER_URL="https://free-api.heweather.com/v5/weather?";

}
