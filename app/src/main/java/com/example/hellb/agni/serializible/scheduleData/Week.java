package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Pair;

import com.example.hellb.agni.serializible.DataProcess;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 07.10.2015.
 */
public class Week extends Observable implements DataProcess, Serializable, Cloneable {
    private static String postDataName = "year_week_number";
    public Group owner;
    public boolean isCurrent;
    private int postData;
    private String weekName;
    public volatile boolean isLoaded;
    public Schedule schedule;

    public Week(Integer post, String group) {
        postData = post;
        weekName = group;
        isCurrent = false;
        schedule = new Schedule();
        schedule.owner = this;
    }

    public Pair<String, Integer> getRequestData() {
        return new Pair<String, Integer>(postDataName, postData);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Week w = new Week(this.postData, this.toString());
        w.schedule = (Schedule) schedule.clone();
        return w;
    }

    @Override
    public void processData(Context con, Observer observer) {
        addObserver(observer);

    }

    @Override
    public boolean equals(Object o) {
        Week week = (Week) o;
        if (week.weekName.equals(this.weekName) &&
                week.postData == this.postData)
            return true;
        else return false;
    }

    @Override
    public String toString() {
        return weekName;
    }

}
