package com.tianjin.MobileInspection.databasetable;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.entity.LocationInfo;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by 吴昶 on 2016-11-24.
 */
public class TLocationInfo implements TBase<LocationInfo> {

    private String TAG = "TLocationInfo";
    private DbManager db;

    public TLocationInfo(DbManager db) {
        this.db = db;
    }

    @Override
    public void create() {
        try {
            db.create(LocationInfo.class);
        } catch (DbException e) {
            KLog.d(e.getMessage());
        }
    }

    @Override
    public int insert(LocationInfo object) {
        try {
            db.save(object);
        } catch (DbException e) {
            KLog.d(e.getMessage());
            return -1;
        }
        return 1;
    }

    @Override
    public void insertList(List<LocationInfo> object) {
        try {
            for (LocationInfo info : object) {
                db.save(info);
            }
        } catch (DbException e) {
            KLog.d(e.getMessage());
        }
    }

    @Override
    public void updata(LocationInfo object) {

    }

    @Override
    public void delete(String name, String values) {
        try {
            db.delete(LocationInfo.class, WhereBuilder.b(name, "=", values));
        } catch (DbException e) {
            KLog.d(e.getMessage());
        }
    }

    @Override
    public List<LocationInfo> query(Object... param) {
        List<LocationInfo> data = null;
        try {
            if (param == null || param.length < 2) {
                data = db.selector(LocationInfo.class).findAll();
            } else {
                data = db.selector(LocationInfo.class).where(WhereBuilder.b(param[0].toString(), "=", param[1].toString())).findAll();
            }
        } catch (DbException e) {
            KLog.d(e.getMessage());
        }
        return data;
    }

    @Override
    public void dropTable() {
        try {
            db.dropTable(LocationInfo.class);
        } catch (DbException e) {
            KLog.d(e.getMessage());
        }
    }
}
