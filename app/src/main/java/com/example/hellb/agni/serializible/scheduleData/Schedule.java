package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Log;

import com.example.hellb.agni.serializible.DataProcess;
import com.example.hellb.agni.serializible.InputStreamToStringWin1251;
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
    private ArrayList<Lesson> lessons;
    private boolean isReady;
    public Week owner;

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public Schedule() {
        lessons = new ArrayList<Lesson>();
        isReady = false;
    }

    public boolean isReady() {
        return isReady;
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
                Elements rowElements = table.getElementsByTag("th"), lessons = new Elements(),
                        tr = table.getElementsByTag("tr");
                ArrayList<String> times = new ArrayList<String>(), days = new ArrayList<String>();

                for (Element element: rowElements)
                {
                    if (element.hasAttr("align"))
                    {
                        times.add(element.text());
                    }
                    else if (element.hasAttr("width"))
                    {
                        days.add(element.text());
                    }
                }
                days.remove(0);

                for (Element element: tr)
                {
                    if (element.getElementsByTag("th").hasText())
                    {
                        lessons.add(element);
                    }
                }
                lessons.remove(0);

                for (int i = 0; i < times.size(); ++i)
                {
                    Elements tmp = lessons.get(i).getElementsByTag("td"), lessonsByDay = new Elements();
                    for (Element element: tmp)
                    {
                        if (element.hasAttr("align"))
                        {
                            lessonsByDay.add(element);
                        }
                    }

                    for (int j = 0; j < days.size(); ++j)
                    {
                        Elements finalLessons = lessonsByDay.get(j).getElementsByTag("td");
                        finalLessons.remove(0);
                        for (Element element: finalLessons)
                        {
                            if (! element.html().isEmpty())
                            {
                                if (element.html().indexOf("slt_gr_wl") == -1)
                                {
                                    this.lessons.add(getLesson(times, days, i, j, element));
                                }
                                else
                                {

                                }
                            }
                        }
                    }
                }
            }

            setChanged();
            notifyObservers(this);
            deleteObservers();
            isReady = true;
        }
        catch (Exception ex){
            Log.d(ex.getMessage(), ex.getLocalizedMessage());
        }
    }

    private Lesson getLesson(ArrayList<String> times, ArrayList<String> days, int i, int j, Element element) {
        String group;
        if (element.html().indexOf("</span> /") != -1)
        {
            group = element.html().substring(element.html().indexOf("</span> /") + 10, element.html().indexOf("<br />"));
        }
        else
        {
            group = "1";
        }
        return Lesson.newBuilder()
                .setCurrentPair(i, times.get(i))
                .setCurrentDay(j, days.get(i))
                .setName(element.getElementsByTag("span").get(0).text(),
                        element.getElementsByTag("span").get(0).attr("title"))
                .setPairType(element.getElementsByTag("span").get(1).text(),
                        element.getElementsByTag("span").get(1).attr("title"))
                .setauditoryNumber(element.getElementsByClass("aud_number").text())
                .setTeacherName(element.getElementsByTag("span").get(3).text(),
                        element.getElementsByTag("span").get(3).attr("title"))
                .setCurrentHalfGroup(Integer.parseInt(group), group)
                .build();
    }
}
