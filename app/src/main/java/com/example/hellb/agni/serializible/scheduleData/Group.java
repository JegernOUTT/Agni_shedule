package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;

import com.example.hellb.agni.serializible.DataProcess;

/**
 * Created by hellb on 06.10.2015.
 */
public class Group implements DataProcess{
    private Integer postData;
    private String groupName;
    private Course owner;

    public Group(Integer post, String group)
    {
        postData = post;
        groupName = group;
    }

    @Override
    public String toString() {
        return  groupName;
    }

    @Override
    public void processData(Context context) {
        /*
        * Получение недель и расписания
        * */
    }
}
