package com.tianjin.MobileInspection.databasetable;

import com.tianjin.MobileInspection.entity.NotificationMsg;
import com.tianjin.MobileInspection.entity.Todo;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by 吴昶 on 2017-8-8.
 */
public class TTodo implements TBase<Todo> {

    public static final String HAS_READ="hasRead";
    public static final String MSG_ID="msgId";
    private DbManager db;

    public TTodo(DbManager db){
        this.db=db;
    }

    @Override
    public void create() {
        try {
            db.create(Todo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(Todo object) {
        try {
            db.save(object);
        } catch (DbException e) {
            return -1;
        }
        return 0;
    }

    @Override
    public void insertList(List<Todo> object) {
        try {
            for(Todo msg:object) {
                db.save(msg);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updata(Todo object) {
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
    public List<Todo> query(Object... param) {
        List<Todo> data=null;
        try {
            if(param==null||param.length==0){
                data=db.selector(Todo.class).findAll();
            }else {
                data=db.selector(Todo.class).where(WhereBuilder.b(param[0].toString(),"=",param[1].toString())).findAll();
            }
        } catch (DbException e) {
            return data;
        }
        return data;
    }

    /**
     * 清空表数据
     */
    @Override
    public void dropTable() {
        try {
            db.delete(Todo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
