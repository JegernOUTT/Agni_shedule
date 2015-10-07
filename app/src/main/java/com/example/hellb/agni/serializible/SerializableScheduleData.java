package com.example.hellb.agni.serializible;

import android.content.Context;

import com.example.hellb.agni.serializible.scheduleData.Faculty;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by hellb on 06.10.2015.
 */
public class SerializableScheduleData implements DataProcess, FutureCallback<InputStream> {
    public String sheduleUrl = "http://is.agni-rt.ru:8080/schedule/";
    private static Context context;


    private static SerializableScheduleData object;
    public volatile boolean isRegisterParamReady;

    private ArrayList<Faculty> faculties;
    public void addFaculty(Faculty fac) {
        this.faculties.add(fac);
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }

    private SerializableScheduleData()
    {
        faculties = new ArrayList<Faculty>();
        isRegisterParamReady = false;
    }

    public static SerializableScheduleData getInstance(Context cont)
    {
        if (object == null)
        {
            object = new SerializableScheduleData();
            context = cont;
        }

        return  object;
    }

    public static SerializableScheduleData getInstance()
    {
        if (object == null)
        {
            object = new SerializableScheduleData();
        }

        return  object;
    }

    @Override
    public void processData(Context context) {
        Ion.with(context)
                .load(sheduleUrl)
                .asInputStream()
                .setCallback(this);
        /*
        * У единственного объекта SerializableScheduleData (singleton) есть ссылка
        * В этом методе должен заполниться массив faculties
        * */
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
        if (result != null)
        {
            Document document = Jsoup.parse(InputStreamToStringWin1251.toStr(result));
            Elements text = document.getElementsByClass("text");
            Elements keys = text.get(1).getElementsByTag("a");

            for (Element link : keys) {
                String str = link.attr("href");
                int start = str.indexOf("faculty_id.value='") + 18;
                int end = str.indexOf("';", start);

                this.addFaculty(new Faculty(Integer.parseInt(str.substring(start, end)), link.text()));
            }
        }

        SerializableScheduleData.getInstance().isRegisterParamReady = true;

        for (Faculty faculty: faculties)
        {
            faculty.processData(context);
        }
    }
}
