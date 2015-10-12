package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;

import com.example.hellb.agni.serializible.DataProcess;
import com.example.hellb.agni.serializible.InputStreamToStringWin1251;
import com.example.hellb.agni.serializible.SerializableScheduleData;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 08.10.2015.
 */
public class Schedule extends Observable implements Serializable, DataProcess, FutureCallback<InputStream>,
    Cloneable
{
    ArrayList<Lesson> lessons;
    public Week owner;

    public Schedule()
    {
        lessons = new ArrayList<Lesson>();
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        Schedule g = new Schedule();
        for (Lesson lesson: lessons)
        {
            g.lessons.add(lesson);
        }

        return g;
    }

    @Override
    public void processData(Context context, Observer observer) {
        addObserver(observer);

        Ion.with(context)
                .load(SerializableScheduleData.getInstance().sheduleUrl)
                .setBodyParameter(owner.owner.owner.owner.getRequestData().first, owner.owner.owner.owner.getRequestData().second.toString())
                .setBodyParameter(owner.owner.getRequestData().first, owner.owner.getRequestData().second.toString())
                .setBodyParameter(owner.getRequestData().first, owner.getRequestData().second.toString())
                .asInputStream()
                .setCallback(this);
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
        try
        {
            if (result != null)
            {
                Document document = Jsoup.parse(InputStreamToStringWin1251.toStr(result));
                Element table = document.getElementsByClass("slt").first();

                Elements rowElements = table.getElementsByTag("th");
                Elements days = new Elements(), time = new Elements(), lessons = new Elements();

                for (Element element: rowElements)
                {
                    if (element.hasAttr("align"))
                    {
                        time.add(element);
                    }
                    else if (element.hasAttr("width"))
                    {
                        days.add(element);
                    }
                }
                days.remove(0);

                Elements tr = table.getElementsByTag("tr");
                for (Element element: tr)
                {
                    if (element.getElementsByTag("th").hasText())
                    {
                        lessons.add(element);
                    }
                }


                Elements sad = table.getElementsByTag("th");







            }
        }
        catch (Exception ex){}
    }
}
