package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.hellb.agni.serializible.DataProcess;
import com.koushikdutta.async.future.FutureCallback;

import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 07.10.2015.
 */
public class Week extends Observable implements DataProcess, FutureCallback<InputStream> {
    private static String postDataName = "year_week_number";
    public Group owner;
    public boolean isCurrent;
    private int postData;
    private String weekName;
    private Context context;
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
        context = con;
        addObserver(observer);

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
