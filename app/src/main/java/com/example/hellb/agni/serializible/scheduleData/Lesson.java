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
    private Pair<String, DaysOfWeek> currentDay;
    private Pair<String, PairTime> currentPair;
    private Pair<String, HalfGroup> currentHalfGroup;
    private Pair<String, String> name;                  //name + fullname
    private Pair<String, String> pairType;
    private String auditoryNumber;
    private Pair<String, String> teacherName;

    public Pair<String, DaysOfWeek> getCurrentDay() {
        return currentDay;
    }

    public Pair<String, PairTime> getCurrentPair() {
        return currentPair;
    }

    public Pair<String, HalfGroup> getCurrentHalfGroup() {
        return currentHalfGroup;
    }

    public Pair<String, String> getName() {
        return name;
    }

    public Pair<String, String> getPairType() {
        return pairType;
    }

    public String getAuditoryNumber() {
        return auditoryNumber;
    }

    public Pair<String, String> getTeacherName() {
        return teacherName;
    }

    private Lesson()
    {

    }

    public static Builder newBuilder() {
        return new Lesson().new Builder();
    }

    public class Builder {
        private Builder(){}

        public Builder setCurrentDay(int cnt, String token) {
            int i = 0;
            for (DaysOfWeek daysOfWeek: DaysOfWeek.values())
            {
                if (i == cnt)
                {
                    Lesson.this.currentDay = new Pair<>(token, daysOfWeek);
                }
                ++i;
            }
            return this;
        }

        public Builder setCurrentPair(int cnt, String token) {
            int i = 0;
            for (PairTime pairTime: PairTime.values())
            {
                if (i == cnt)
                {
                    Lesson.this.currentPair = new Pair<>(token, pairTime);
                }
                ++i;
            }
            return this;
        }

        public Builder setCurrentHalfGroup(int cnt, String token){
            int i = 0; --cnt;
            for (HalfGroup halfGroup: HalfGroup.values())
            {
                if (i == cnt)
                {
                    Lesson.this.currentHalfGroup = new Pair<>(token, halfGroup);
                }
                ++i;
            }
            return this;
        }

        public Builder setName(String name, String fullName) {
            Lesson.this.name = new Pair<>(name, fullName);
            return this;
        }

        public Builder setPairType(String name, String fullName) {
            Lesson.this.pairType = new Pair<>(name, fullName);
            return this;
        }

        public Builder setTeacherName(String name, String fullName) {
            Lesson.this.teacherName = new Pair<>(name, fullName);
            return this;
        }

        public Builder setauditoryNumber(String number) {
            Lesson.this.auditoryNumber = new String(number);
            return this;
        }

        public Lesson build() {
            return Lesson.this;
        }
    }
}
