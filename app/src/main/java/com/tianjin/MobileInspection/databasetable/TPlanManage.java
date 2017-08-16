package com.tianjin.MobileInspection.databasetable;

import com.tianjin.MobileInspection.entity.PlanManage;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by wuchang on 2016-12-30.
 */
public class TPlanManage implements TBase<PlanManage>{

    private DbManager db;

    public TPlanManage(DbManager db){
        this.db=db;
    }

    @Override
    public void create() {
        try {
            db.create(PlanManage.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(PlanManage object) {
        try {
            db.save(object);
        } catch (DbException e) {
            return -1;
        }
        return 1;
    }

    @Override
    public void insertList(List<PlanManage> object) {
        try {
            for(PlanManage pm:object) {
                db.save(pm);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updata(PlanManage object) {

    }

    @Override
    public void delete(String name, String values) {

    }

    @Override
    public List<PlanManage> query(Object... param) {
        List<PlanManage> data=null;
        try {
             if(param==null||param.length==0){
                data=db.selector(PlanManage.class).findAll();
            }else {
                data=db.selector(PlanManage.class).where(WhereBuilder.b("planId","=",param[0].toString())).findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return data;
    }

    //根据页码
    public List<PlanManage> query(int pageNo,int pageSize,String planType) {
        List<PlanManage> data=null;
        try {
            if(pageNo==1){
                data=db.selector(PlanManage.class).where(WhereBuilder.b("id","<",pageSize))
                        .where(WhereBuilder.b("planType","=",planType)).orderBy("createDate",false).findAll();
            }else {
                data=db.selector(PlanManage.class).where(WhereBuilder.b("id",">=",pageSize*(pageNo-1)))
                        .where(WhereBuilder.b("planType","=",planType)).where(WhereBuilder.b("id","<",pageSize*pageNo)).orderBy("createDate",false).findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return data;
    }



    @Override
    public void dropTable() {
        try {
            db.dropTable(PlanManage.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
