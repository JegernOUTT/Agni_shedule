package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

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
import java.util.ArrayList;

/**
 * Created by hellb on 06.10.2015.
 */
public class Group implements DataProcess, FutureCallback<InputStream> {
    private static String postDataName = "group_id";
    private Integer postData;
    private String groupName;
    private ArrayList<Week> weeks;
    public Course owner;
    public volatile boolean isLoaded;

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
    public String toString() {
        return  groupName;
    }

    @Override
    public void processData(Context context) {
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
        }
        catch (Exception ex)
        {
            Log.e("Parce Error", ex.getMessage());
        }
    }
}
