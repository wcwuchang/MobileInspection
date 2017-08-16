package com.tianjin.MobileInspection.until;

import android.util.Xml;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.entity.Info;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吴昶 on 2017/6/30.
 */
public class PullPersonService {

    /**
     * ------------------------使用PULL解析XML-----------------------
     * @param inStream
     * @return
     * @throws Exception
     */
    public static List<Info> getPersons(InputStream inStream) {
        List<Info> persons = null;
        try {
            Info info = null;
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inStream, "UTF-8");
            int event = pullParser.getEventType();// 觸發第一個事件
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        persons = new ArrayList<Info>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("version".equals(pullParser.getName())) {
                            KLog.d(pullParser.getName());
                            String id = pullParser.nextText();
                            info = new Info();
                            info.setVersion(id);
                        }
                        if (info != null) {
                            if ("url".equals(pullParser.getName())) {
                                info.setUrl(pullParser.nextText());
                            }
                            if ("des".equals(pullParser.getName())) {
                                info.setDes(pullParser.nextText());
                            }
                            if ("type".equals(pullParser.getName())) {
                                info.setType(pullParser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("info".equals(pullParser.getName())) {
                            persons.add(info);
                            info = null;
                        }
                        break;
                }
                event = pullParser.next();
            }
        }catch (Exception e){
            KLog.d(e.getMessage());
            return null;
        }
        return persons;
    }
}
