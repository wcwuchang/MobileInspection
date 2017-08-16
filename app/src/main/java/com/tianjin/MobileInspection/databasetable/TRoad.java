package com.tianjin.MobileInspection.databasetable;

import android.util.Log;

import com.tianjin.MobileInspection.entity.HiddenSpinner;
import com.tianjin.MobileInspection.entity.Inspection;
import com.tianjin.MobileInspection.entity.Road;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by 吴昶 on 2017/6/5.
 */
public class TRoad implements TBase<Road> {

    private String TAG="THiddenSpinner";
    private DbManager db;

    public TRoad(DbManager db){
        this.db=db;
    }

    @Override
    public void create() {
        try {
            db.create(Road.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(Road object) {
        try {
            db.save(object);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void insertList(List<Road> object) {
        try {
            for(Road road:object) {
                db.save(road);
            }
        } catch (DbException e) {
            Log.e(TAG, "insertList eror: " +e.getMessage());
        }
    }

    @Override
    public void updata(Road object) {
        try {
            db.saveOrUpdate(object);
        } catch (DbException e) {
            Log.e(TAG, "updata eror: " +e.getMessage());
        }
    }

    @Override
    public void delete(String name, String values) {
        try {
            db.delete(Road.class, WhereBuilder.b(name,"=",values));
        } catch (DbException e) {
            Log.e(TAG, "delete eror: " +e.getMessage());
        }
    }

    @Override
    public List<Road> query(Object... param) {
        List<Road> list=null;
        try {
            if(param==null||param.length==0){
                list=db.selector(Road.class).orderBy("id",false).findAll();
            }else{
                list=db.selector(Road.class)
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
            db.delete(Road.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
