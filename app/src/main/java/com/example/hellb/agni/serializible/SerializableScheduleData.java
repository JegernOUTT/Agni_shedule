package com.example.hellb.agni.serializible;

import com.example.hellb.agni.serializible.scheduleData.Faculty;

import java.util.ArrayList;

/**
 * Created by hellb on 06.10.2015.
 */
public class SerializableScheduleData {
    private static SerializableScheduleData object;
    public volatile boolean isRegisterParamReady;

    private ArrayList<Faculty> faculties;
    public void addFaculty(Faculty fac) {
        this.faculties.add(fac);
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }

    private SerializableScheduleData()
    {
        faculties = new ArrayList<Faculty>();
        isRegisterParamReady = false;
    }

    public static SerializableScheduleData getInstance()
    {
        if (object == null)
        {
            object = new SerializableScheduleData();
        }

        return  object;
    }
}
