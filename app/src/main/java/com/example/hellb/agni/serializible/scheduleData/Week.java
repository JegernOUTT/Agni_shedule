package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.hellb.agni.serializible.DataProcess;
import com.koushikdutta.async.future.FutureCallback;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 07.10.2015.
 */
public class Week extends Observable implements DataProcess, FutureCallback<InputStream>, Serializable {
    private static String postDataName = "year_week_number";
    public transient Group owner;
    public boolean isCurrent;
    private int postData;
    private String weekName;
    public volatile boolean isLoaded;

    public Week(Integer post, String group) {
        postData = post;
        weekName = group;
        isCurrent = false;
    }

    public Pair<String, Integer> getRequestData() {
        return new Pair<String, Integer>(postDataName, postData);
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

    @Override
    public void onCompleted(Exception e, InputStream result) {
        try
        {

            notifyObservers(this);
            deleteObservers();
        }
        catch (Exception ex)
        {
            deleteObservers();
            Log.e("Parce Error", ex.getMessage());
        }
    }
}
