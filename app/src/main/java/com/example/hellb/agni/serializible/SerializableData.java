package com.example.hellb.agni.serializible;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hellb on 06.10.2015.
 */
public class SerializableData {
    public volatile boolean isRegisterParamReady;

    private static SerializableData object;

    private Map scheduleInputParams;
    public void setScheduleInputParams(Map scheduleInputParams) {
        this.scheduleInputParams = scheduleInputParams;
        isRegisterParamReady = true;
    }

    public Map getScheduleInputParams() {
        return scheduleInputParams;
    }

    private SerializableData()
    {
        scheduleInputParams = new HashMap<Integer, String>();
        isRegisterParamReady = false;
    }

    public static SerializableData getInstance()
    {
        if (object == null)
        {
            object = new SerializableData();
        }

        return  object;
    }
}
