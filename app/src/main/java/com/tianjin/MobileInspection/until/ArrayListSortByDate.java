package com.tianjin.MobileInspection.until;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.entity.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by wuchang on 2016-12-15.
 *
 * arraylist 排序
 */
public class ArrayListSortByDate implements Comparator{
    @Override
    public int compare(Object lhs, Object rhs) {
        Task i1=(Task)lhs;
        Task i2=(Task)rhs;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        //根据时间排序（降）
        Date date1;
        Date date2;
        try {
            date1=sdf.parse(i1.getDate());
            date2=sdf.parse(i2.getDate());
            if(date2.before(date1)){
                return -1;//后一个比前一个早返回-1（由大到小（降序）），否则返回1（由小到大（升序））
            }else {
                return 1;
            }
        } catch (ParseException e) {
            KLog.d(e.getMessage());
        }
        return 0;
    }
}
