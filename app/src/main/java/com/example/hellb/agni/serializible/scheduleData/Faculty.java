package com.example.hellb.agni.serializible.scheduleData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hellb on 06.10.2015.
 */
public class Faculty {
    private Integer postData;
    private String facultyName;
    public List<Course> courses;

    public Faculty(Integer post, String faculty)
    {
        postData = post;
        facultyName = faculty;
        courses = new ArrayList<Course>();
    }

    @Override
    public String toString() {
        return facultyName;
    }

    public String[] getStringRepresentationCourses()
    {
        String[] strings = new String[courses.size()];
        Iterator<Course> courseIterator = courses.iterator();
        for (int i = 0; i < courses.size(); ++i)
        {
            strings[i] = courseIterator.next().toString();
        }

        return strings;
    }
}
