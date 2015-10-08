package com.example.hellb.agni.serializible;

import android.content.Context;

import com.example.hellb.agni.DataGetStack;
import com.example.hellb.agni.serializible.scheduleData.Faculty;
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
public class SerializableScheduleData extends Observable implements DataProcess, FutureCallback<InputStream> {
    public String sheduleUrl = "http://is.agni-rt.ru:8080/schedule/";
    private Context context;
    DataGetStack dataGetStack = DataGetStack.getInstance();

    private static SerializableScheduleData object;
    public boolean isLoaded() {
        boolean isLoaded = true;
        for (Faculty faculty: faculties)
        {
            if (! faculty.isLoaded())
            {
                isLoaded = false;
            }
        }

        if (faculties.size() == 0)
            return false;

        return  isLoaded;
    };

    private ArrayList<Faculty> faculties;
    public void addFaculty(Faculty fac) {
        this.faculties.add(fac);
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }

    private SerializableScheduleData() {
        faculties = new ArrayList<Faculty>();
    }

    public static SerializableScheduleData getInstance() {
        if (object == null)
        {
            object = new SerializableScheduleData();
        }

        return  object;
    }

    public String[] getStringRepresentationFaculties() {
        String[] strings = new String[faculties.size()];
        Iterator<Faculty> courseIterator = faculties.iterator();
        for (int i = 0; i < faculties.size(); ++i)
        {
            strings[i] = courseIterator.next().toString();
        }

        return strings;
    }

    @Override
    public void processData(Context cont, Observer observer) {
        addObserver(observer);
        context = cont;

        Ion.with(context)
                .load(sheduleUrl)
                .asInputStream()
                .setCallback(this);
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
        if (result != null)
        {
            Document document = Jsoup.parse(InputStreamToStringWin1251.toStr(result));
            Elements keys = document.getElementsByClass("text")
                    .last()
                    .getElementsByTag("a");

            for (Element link : keys) {
                String str = link.attr("href");
                int start = str.indexOf("faculty_id.value='") + 18;
                int end = str.indexOf("';", start);

                addFaculty(new Faculty(Integer.parseInt(str.substring(start, end)), link.text()));
            }
        }

        for (Faculty faculty: faculties)
        {
            dataGetStack.addTask(faculty);
        }

        setChanged();
        notifyObservers(this);
        deleteObservers();
    }
}
