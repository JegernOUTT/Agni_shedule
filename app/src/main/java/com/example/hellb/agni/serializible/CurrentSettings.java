package com.example.hellb.agni.serializible;

import com.example.hellb.agni.serializible.scheduleData.Course;
import com.example.hellb.agni.serializible.scheduleData.Faculty;
import com.example.hellb.agni.serializible.scheduleData.Group;
import com.example.hellb.agni.serializible.scheduleData.Week;

/**
 * Created by hellb on 07.10.2015.
 */
public class CurrentSettings {
    public Faculty faculty;
    public Course course;
    public Group group;
    public Week week;

    private static CurrentSettings ourInstance = new CurrentSettings();

    public static CurrentSettings getInstance() {
        return ourInstance;
    }

    private CurrentSettings() {
    }
}
