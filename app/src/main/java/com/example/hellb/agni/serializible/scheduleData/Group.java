package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

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
 * Created by hellb on 06.10.2015.
 */
public class Group extends Observable implements DataProcess, FutureCallback<InputStream>, Serializable,
    Cloneable
{
    private static String postDataName = "group_id";
    private Integer postData;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Group g = new Group(this.postData, this.toString());
        for (Week week: this.getWeeks())
        {
            g.addWeek((Week) week.clone());
        }

        return g;
    }

    private String groupName;
    private ArrayList<Week> weeks;
    public Course owner;

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    private volatile boolean isLoaded;
    public boolean isLoaded() {
        return isLoaded;
    };

    public Group(Integer post, String group) {
        postData = post;
        groupName = group;
        weeks = new ArrayList<Week>();
    }

    public ArrayList<Week> getWeeks() {
        return weeks;
    }

    public Pair<String, Integer> getRequestData() {
        return new Pair<String, Integer>(postDataName, postData);
    }

    public void addWeek(Week week) {
        week.owner = this;
        weeks.add(week);
    }

    @Override
    public boolean equals(Object o) {
        Group group = (Group) o;
        if (group.groupName.equals(this.groupName) &&
                group.postData == this.postData)
            return true;
        else return false;
    }

    @Override
    public String toString() {
        return  groupName;
    }

    @Override
    public void processData(Context context, Observer observer) {
        addObserver(observer);

        Ion.with(context)
                .load(SerializableScheduleData.getInstance().sheduleUrl)
                .setBodyParameter(owner.owner.getRequestData().first, owner.owner.getRequestData().second.toString())
                .setBodyParameter(postDataName, postData.toString())
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
                Elements optionElements = document.getElementsByTag("select")
                        .last()
                        .getElementsByTag("option");

                for (Element element: optionElements)
                {
                    Week week = new Week(Integer.parseInt(element.val()), element.text());
                    if (element.attr("selected").indexOf("selected") == 0)
                        week.isCurrent = true;
                    addWeek(week);
                }

                isLoaded = true;
            }

            setChanged();
            notifyObservers(this);
            deleteObservers();
        }
        catch (Exception ex)
        {
            Log.e("Parce Error", ex.getMessage());
            deleteObservers();
        }
    }

    @Nullable
    public Object getObjectByString(String name) {
        for (Week week: weeks)
        {
            if (week.toString().equals(name))
            {
                return week;
            }
        }
        return null;
    }
}
