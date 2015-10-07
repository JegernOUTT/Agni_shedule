package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;

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
import java.util.List;

/**
 * Created by hellb on 06.10.2015.
 */
public class Faculty implements DataProcess, FutureCallback<InputStream> {
    public static String postDataName = "faculty_id";
    private Integer postData;
    private String facultyName;
    public List<Course> courses;
    public volatile boolean isRegisterParamReady;

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

    @Override
    public void processData(Context context) {
        /*
        * У каждого объекта Faculty есть postData
        * В этом методе должно создаться POST запрос и после
        * этого должен заполниться массив courses
        * */
        Ion.with(context)
                .load(SerializableScheduleData.getInstance().sheduleUrl)
                .setBodyParameter(postDataName, postData.toString())
                .asInputStream()
                .setCallback(this);
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
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
                    course.groups.add(new Group(Integer.parseInt(str.substring(start, end)), groupElement.text()));
                }
                ++i;
                courses.add(course);
            }

            this.isRegisterParamReady = true;
        }
    }
}
