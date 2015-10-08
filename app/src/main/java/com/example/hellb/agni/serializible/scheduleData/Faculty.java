package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.hellb.agni.DataGetStack;
import com.example.hellb.agni.serializible.DataProcess;
import com.example.hellb.agni.serializible.InputStreamToStringWin1251;
import com.example.hellb.agni.serializible.SerializableScheduleData;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 06.10.2015.
 */
public class Faculty extends Observable implements DataProcess, FutureCallback<InputStream> {
    private static String postDataName = "faculty_id";
    private Integer postData;
    private String facultyName;
    private Context context;
    DataGetStack dataGetStack = DataGetStack.getInstance();

    public ArrayList<Course> getCourses() {
        return courses;
    }

    private ArrayList<Course> courses;
    public boolean isLoaded() {
        boolean isLoaded = true;

        for (Course course: courses)
        {
            for (Group group: course.getGroups())
            {
                if (! group.isLoaded())
                {
                    isLoaded = false;
                }
            }
        }

        return isLoaded;
    }

    public Faculty(Integer post, String faculty) {
        postData = post;
        facultyName = faculty;
        courses = new ArrayList<Course>();
    }

    public String[] getStringRepresentationCourses() {
        String[] strings = new String[courses.size()];
        Iterator<Course> courseIterator = courses.iterator();
        for (int i = 0; i < courses.size(); ++i)
        {
            strings[i] = courseIterator.next().toString();
        }

        return strings;
    }

    public Pair<String, Integer> getRequestData() {
        return new Pair<String, Integer>(postDataName, postData);
    }

    public void addCourse(Course course) {
        course.owner = this;
        courses.add(course);
    }

    @Override
    public String toString() {
        return facultyName;
    }

    @Override
    public void processData(Context cont,  Observer observer) {
        context = cont;
        addObserver(observer);
        Ion.with(context)
                .load(SerializableScheduleData.getInstance().sheduleUrl)
                .setBodyParameter(postDataName, postData.toString())
                .asInputStream()
                .setCallback(this);
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
        try
        {
            if (result != null)
            {
                Document document = Jsoup.parse(InputStreamToStringWin1251.toStr(result));
                Elements elementsByTag = document
                        .getElementsByTag("table")
                        .last()
                        .getElementsByTag("td");

                Iterator<Element> elementIterator = elementsByTag.iterator();
                for (int i = 0; i < elementsByTag.size(); ++i) {
                    Course course = new Course(elementIterator.next().text());
                    Elements groups = elementIterator.next().getElementsByTag("a");

                    for (Element groupElement: groups)
                    {
                        String str = groupElement.attr("href");
                        int start = str.indexOf("sp_student.group_id.value='") + 27;
                        int end = str.indexOf("';", start);
                        course.addGroup(new Group(Integer.parseInt(str.substring(start, end)), groupElement.text()));
                    }
                    ++i;
                    addCourse(course);
                }
            }

            for (Course course: courses)
            {
                for (Group group: course.getGroups())
                {
                    dataGetStack.addTask(group);
                }
            }

            setChanged();
            notifyObservers(this);
            deleteObservers();
        }
        catch (Exception ex)
        {
            Log.e("Parce Error", ex.getMessage());
            deleteObservers();
        }
    }
}
