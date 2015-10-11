package com.example.hellb.agni.serializible.scheduleData;

import android.util.Pair;

import com.example.hellb.agni.serializible.scheduleEnums.DaysOfWeek;

import java.io.Serializable;

/**
 * Created by hellb on 09.10.2015.
 */
public class Lesson implements Serializable {
    Pair<String, DaysOfWeek> currentDay;


}
