package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Pair;

import com.example.hellb.agni.serializible.DataProcess;
import com.koushikdutta.async.future.FutureCallback;

import java.io.InputStream;

/**
 * Created by hellb on 07.10.2015.
 */
public class Week implements DataProcess, FutureCallback<InputStream> {
    private static String postDataName = "year_week_number";
    public Group owner;
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
    public void processData(Context context) {

    }

    @Override
    public void onCompleted(Exception e, InputStream result) {

    }
}
