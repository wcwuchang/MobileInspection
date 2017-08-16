package com.tianjin.MobileInspection.databasetable;

import android.util.Log;

import com.tianjin.MobileInspection.entity.Inspection;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by wuchang on 2016/10/14.
 */
public class TInspection implements TBase<Inspection> {

    private String TAG="TInspection";
    private DbManager db;

    public TInspection(DbManager db){
        this.db=db;
    }

    @Override
    public void create() {
        try {
            db.create(Inspection.class);
        } catch (DbException e) {
            Log.e(TAG, "create eror: " +e.getMessage());
        }
    }

    @Override
    public int insert(Inspection object) {
        try {
            db.save(object);
        } catch (DbException e) {
            Log.e(TAG, "insert eror: " +e.getMessage());
            return -1;
        }
        return 1;
    }

    @Override
    public void insertList(List<Inspection> object) {
        try {
            for(Inspection inspection:object) {
                db.save(inspection);
            }
        } catch (DbException e) {
            Log.e(TAG, "insertList eror: " +e.getMessage());
        }
    }

    @Override
    public void updata(Inspection object) {
        try {
            db.saveOrUpdate(object);
        } catch (DbException e) {
            Log.e(TAG, "updata eror: " +e.getMessage());
        }
    }

    @Override
    public void delete(String name, String values) {
        try {
            db.delete(Inspection.class, WhereBuilder.b(name,"=",values));
        } catch (DbException e) {
            Log.e(TAG, "delete eror: " +e.getMessage());
        }
    }

    @Override
    public List<Inspection> query(Object... param) {
        List<Inspection> list=null;
        try {
            if(param==null||param.length==0){
                list=db.selector(Inspection.class).orderBy("id",false).findAll();
             }else{
                list=db.selector(Inspection.class)
                        .where(WhereBuilder.b(param[0].toString(),"=",param[1].toString()))
                        .orderBy("id",false).findAll();
            }
         }catch (DbException e) {
             e.printStackTrace();
         }
        return list;
    }

    @Override
    public void dropTable() {
        try {
            db.dropTable(Inspection.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
