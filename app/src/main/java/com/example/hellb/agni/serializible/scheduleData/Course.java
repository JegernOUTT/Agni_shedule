package com.example.hellb.agni.serializible.scheduleData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hellb on 06.10.2015.
 */
public class Course {
    public List<Group> getGroups() {
        return groups;
    }

    private List<Group> groups;
    private String courseName;
    public Faculty owner;

    Course(String name) {
        courseName = name;
        groups = new ArrayList<Group>();
    }

    public void addGroup(Group group) {
        group.owner = this;
        groups.add(group);
    }

    @Override
    public String toString() {
        return  courseName;
    }

    public String[] getStringRepresentationGroups()
    {
        String[] strings = new String[groups.size()];
        Iterator<Group> courseIterator = groups.iterator();
        for (int i = 0; i < groups.size(); ++i)
        {
            strings[i] = courseIterator.next().toString();
        }

        return strings;
    }
}
