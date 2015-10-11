package com.example.hellb.agni.serializible.scheduleData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hellb on 08.10.2015.
 */
public class Schedule implements Serializable {
    ArrayList<Lesson> lessons;

    public Schedule()
    {
        lessons = new ArrayList<Lesson>();
    }





}
