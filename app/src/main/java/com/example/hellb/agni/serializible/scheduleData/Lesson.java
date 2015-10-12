package com.example.hellb.agni.serializible.scheduleData;

import android.util.Pair;

import com.example.hellb.agni.serializible.scheduleEnums.DaysOfWeek;
import com.example.hellb.agni.serializible.scheduleEnums.HalfGroup;
import com.example.hellb.agni.serializible.scheduleEnums.PairTime;

import java.io.Serializable;

/**
 * Created by hellb on 09.10.2015.
 */
public class Lesson implements Serializable {
    Pair<String, DaysOfWeek> currentDay;
    Pair<String, PairTime> currentPair;
    Pair<String, HalfGroup> currentHalfGroup;
    Pair<String, String> name;                  //name + fullname
    Pair<String, String> pairType;
    String auditoryNumber;
    Pair<String, String> teacherName;
}
