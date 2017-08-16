package com.tianjin.MobileInspection.databasetable;

import com.tianjin.MobileInspection.entity.NotificationMsg;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by 吴昶 on 2017-8-8.
 */
public class TNotificationMsg implements TBase<NotificationMsg> {

    public static final String HAS_READ="hasRead";
    public static final String MSG_ID="msgId";
    private DbManager db;

    public TNotificationMsg(DbManager db){
        this.db=db;
    }

    @Override
    public void create() {
        try {
            db.create(NotificationMsg.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(NotificationMsg object) {
        try {
            db.save(object);
        } catch (DbException e) {
            return -1;
        }
        return 0;
    }

    @Override
    public void insertList(List<NotificationMsg> object) {
        try {
            for(NotificationMsg msg:object) {
                db.save(msg);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updata(NotificationMsg object) {
        try {
            db.saveOrUpdate(object);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String name, String values) {
        try {
            db.delete(NotificationMsg.class, WhereBuilder.b(name,"=",values));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<NotificationMsg> query(Object... param) {
        List<NotificationMsg> data=null;
        try {
            if(param==null||param.length==0){
                data=db.selector(NotificationMsg.class).findAll();
            }else {
                data=db.selector(NotificationMsg.class).where(WhereBuilder.b(param[0].toString(),"=",param[1].toString())).findAll();
            }
        } catch (DbException e) {
            return data;
        }
        return data;
    }

    @Override
    public void dropTable() {
        try {
            db.dropTable(NotificationMsg.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
