package com.tianjin.MobileInspection.databasetable;

import android.util.Log;

import com.tianjin.MobileInspection.entity.InspectionChoose;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by wuchang on 2016/10/13.
 */
public class TStartInspection implements TBase<InspectionChoose> {
    private String TAG = "TStartInspection";
    private DbManager db;

    public TStartInspection(DbManager db) {
        this.db = db;
    }

    @Override
    public void create() {
        try {
            db.create(InspectionChoose.class);
        } catch (DbException e) {
            Log.e(TAG, "create InspectionChoose error " + e.getMessage());
        }
    }

    @Override
    public int insert(InspectionChoose object) {
        try {
            db.save(object);
        } catch (DbException e) {
            Log.e(TAG, "insert InspectionChoose error " + e.getMessage());
            return -1;
        }
        return 1;
    }

    @Override
    public void insertList(List<InspectionChoose> object) {
        try {
            for (InspectionChoose startInspection : object) {
                db.save(startInspection);
            }
        } catch (DbException e) {
            Log.e(TAG, "insert InspectionChoose error " + e.getMessage());
        }
    }

    @Override
    public void updata(InspectionChoose object) {
        try {
            db.saveOrUpdate(object);
        } catch (DbException e) {
            Log.e(TAG, "updata InspectionChoose error " + e.getMessage());
        }
    }

    @Override
    public void delete(String name, String values) {
        try {
            db.delete(InspectionChoose.class, WhereBuilder.b(name, "=", values));
        } catch (DbException e) {
            Log.e(TAG, "delete InspectionChoose error " + e.getMessage());
        }
    }

    @Override
    public List<InspectionChoose> query(Object... param) {
        List<InspectionChoose> data = null;
        try {
            if (param == null || param.length == 0) {
                data = db.selector(InspectionChoose.class).orderBy("id",false).findAll();
            } else {
                data = db.selector(InspectionChoose.class).where(WhereBuilder.b(param[0].toString(), "=", param[1].toString())).orderBy("id",false).findAll();
            }
        } catch (DbException e) {
            Log.e(TAG, "query InspectionChoose error " + e.getMessage());
        }
        return data;
    }

    @Override
    public void dropTable() {
        try {
            db.dropTable(InspectionChoose.class);
        } catch (DbException e) {
            Log.e(TAG, "dropTable InspectionChoose error " + e.getMessage());
        }
    }
}
