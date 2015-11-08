package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hellb on 06.10.2015.
 */
public class Course implements Serializable, Cloneable {
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Course c = new Course(this.toString());
        for (Group group: this.getGroups())
        {
            c.addGroup((Group) group.clone());
        }

        return c;
    }

    public void addGroup(Group group) {
        group.owner = this;
        groups.add(group);
    }

    @Override
    public boolean equals(Object o) {
        Course course = (Course) o;
        if (course.courseName.equals(this.courseName))
            return true;
        else return false;
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

    @Nullable
    public Object getObjectByString(String name) {
        for (Group group: groups)
        {
            if (group.toString().equals(name))
            {
                return group;
            }
        }
        return null;
    }

    public void clear() {
        for (Group group: groups)
        {
            group.clear();
        }
        groups.clear();
    }
}
