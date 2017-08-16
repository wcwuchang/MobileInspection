package com.tianjin.MobileInspection.database;

import android.content.Context;
import android.util.Log;

import com.tianjin.MobileInspection.databasetable.THiddenSpinner;
import com.tianjin.MobileInspection.databasetable.TLocationInfo;
import com.tianjin.MobileInspection.databasetable.TNotificationMsg;
import com.tianjin.MobileInspection.databasetable.TPlanManage;
import com.tianjin.MobileInspection.databasetable.TRoad;
import com.tianjin.MobileInspection.databasetable.TStartInspection;
import com.tianjin.MobileInspection.databasetable.TTodo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

public class DatabaseManager {
	private String TAG="DatabaseManager";
	private DbManager db;
	private TStartInspection tInspection;
	private THiddenSpinner tHiddenSpinner;

	private TLocationInfo tLocationInfo;

	private TPlanManage tPlanManage;

	private TRoad tRoad;

	private TNotificationMsg tNotificationMsg;

	private TTodo tTodo;
	
	public DatabaseManager(Context context) {
		// TODO Auto-generated constructor stub
		x.Ext.setDebug(true);
		db=x.getDb(DataBaseOpenHelper.getDaoConfig(context));
	}

	public TStartInspection gettInspection(){
		if(tInspection==null){
			tInspection=new TStartInspection(db);
		}
		return tInspection;
	}

	public TLocationInfo gettLocationInfo(){
		if(tLocationInfo==null){
			tLocationInfo=new TLocationInfo(db);
		}
		return tLocationInfo;
	}

	public TPlanManage gettPlanManage(){
		if(tPlanManage==null){
			tPlanManage=new TPlanManage(db);
		}
		return tPlanManage;
	}

	public THiddenSpinner gettHiddenSpinner() {
		if(tHiddenSpinner==null){
			tHiddenSpinner=new THiddenSpinner(db);
		}
		return tHiddenSpinner;
	}

	public TRoad gettRoad() {
		if(tRoad==null){
			tRoad=new TRoad(db);
		}
		return tRoad;
	}

	public TNotificationMsg gettNotificationMsg() {
		if(tNotificationMsg==null){
			tNotificationMsg=new TNotificationMsg(db);
		}
		return tNotificationMsg;
	}

	public TTodo gettTodo() {
		if(tTodo==null){
			tTodo=new TTodo(db);
		}
		return tTodo;
	}

	public void dropTable(Class<?> tClass){
		try {
			db.dropTable(tClass);
		} catch (DbException e) {
			Log.e(TAG, "dropTable error: "+e.getMessage());
		}
	}
	
}
