package com.example.hellb.agni.serializible.scheduleData;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.hellb.agni.DataGetStack;
import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.DataProcess;
import com.example.hellb.agni.serializible.InputStreamToStringWin1251;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 08.10.2015.
 */
public class Schedule extends Observable implements Serializable, DataProcess, FutureCallback<InputStream>,
    Cloneable
{
    private ArrayList<Lesson> lessons;
    private boolean isReady;
    public Week owner;
    private transient Context context;

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void clearLessons(){
        lessons.clear();
    }

    public Schedule() {
        lessons = new ArrayList<Lesson>();
        isReady = false;
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Schedule g = new Schedule();
        for (Lesson lesson: lessons)
        {
            g.lessons.add(lesson);
        }

        return g;
    }

    @Override
    public void processData(Context context, Observer observer) {
        addObserver(observer);
        this.context = context;

        Ion.with(context)
                .load(SerializableScheduleData.getInstance().sheduleUrl)
                .setBodyParameter(owner.owner.owner.owner.getRequestData().first, owner.owner.owner.owner.getRequestData().second.toString())
                .setBodyParameter(owner.owner.getRequestData().first, owner.owner.getRequestData().second.toString())
                .setBodyParameter(owner.getRequestData().first, owner.getRequestData().second.toString())
                .asInputStream()
                .setCallback(this);
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
        try
        {
            if (result != null)
            {
                String file = InputStreamToStringWin1251.toStr(result);
                Document document = Jsoup.parse(file);

                Element table = document.getElementsByClass("slt").first();
                if (table == null)
                {
                    createEmptyLesson();
                    return;
                }
                Elements rowElements = table.getElementsByTag("th"), lessons = new Elements(),
                        tr = table.getElementsByTag("tr");
                ArrayList<String> times = new ArrayList<String>(), days = new ArrayList<String>();

                for (Element element: rowElements)
                {
                    if (element.hasAttr("align"))
                    {
                        times.add(element.text());
                    }
                    else if (element.hasAttr("width"))
                    {
                        days.add(element.text());
                    }
                }
                days.remove(0);

                for (Element element: tr)
                {
                    if (element.getElementsByTag("th").hasText())
                    {
                        lessons.add(element);
                    }
                }
                lessons.remove(0);

                for (int i = 0; i < times.size(); ++i)
                {
                    Elements tmp = lessons.get(i).getElementsByTag("td"), lessonsByDay = new Elements();
                    for (Element element: tmp)
                    {
                        if (element.hasAttr("align"))
                        {
                            lessonsByDay.add(element);
                        }
                    }

                    for (int j = 0; j < days.size(); ++j)
                    {
                        Elements finalLessons = lessonsByDay.get(j).getElementsByTag("td");
                        finalLessons.remove(0);
                        if (finalLessons.size() == 3)
                        {
                            Elements elements = finalLessons.get(0).getElementsByTag("span");
                            if (elements.size() == 7){
                                getLessonFrom7Span(times, days, i, j, elements);
                            } else if (elements.size() == 6)
                            {
                                getLessonFrom6Span(times, days, i, j, elements);
                            }
                        }
                        else
                        {
                            for (Element element: finalLessons)
                            {
                                if (! element.html().isEmpty())
                                {
                                    this.lessons.add(getLesson(times, days, i, j, element));
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show();
                DataGetStack.getInstance().clearStack();
            }

            CurrentSettings settings = CurrentSettings.getInstance();

            setChanged();
            notifyObservers(this);
            deleteObservers();
            isReady = true;
        }
        catch (Exception ex){
            Log.e(ex.getMessage(), ex.getLocalizedMessage());
        }
    }

    private void createEmptyLesson() {
        lessons.add(Lesson.newBuilder()
                .setCurrentDay("Понедельник")
                .setName("", "Данные для построения отсутствуют")
                .setCurrentPair(0, "")
                .setauditoryNumber("")
                .setCurrentHalfGroup(3, "")
                .setPairType("","")
                .setTeacherName("","")
                .build());

        setChanged();
        notifyObservers(this);
        deleteObservers();
        isReady = true;
    }

    private void getLessonFrom7Span(ArrayList<String> times, ArrayList<String> days, int i, int j, Elements elements) {
        Lesson.Builder builder1 = Lesson.newBuilder();
        Lesson.Builder builder2 = Lesson.newBuilder();

        this.lessons.add(builder1.setCurrentPair(i, times.get(i))
                .setCurrentDay(days.get(j))
                .setName(elements.get(0).text(),
                        elements.get(0).attr("title"))
                .setPairType(elements.get(1).text(),
                        elements.get(1).attr("title"))
                .setauditoryNumber(elements.get(2).text())
                .setTeacherName(elements.get(3).text(),
                        elements.get(3).attr("title"))
                .setCurrentHalfGroup(1, "1")
                .build());

        this.lessons.add(builder2.setCurrentPair(i, times.get(i))
                .setCurrentDay(days.get(j))
                .setName(elements.get(0).getElementsByTag("span").get(0).text(),
                        elements.get(0).getElementsByTag("span").get(0).attr("title"))
                .setPairType(elements.get(4).text(),
                        elements.get(4).attr("title"))
                .setauditoryNumber(elements.get(5).text())
                .setTeacherName(elements.get(6).text(),
                        elements.get(6).attr("title"))
                .setCurrentHalfGroup(2, "2")
                .build());
    }

    private void getLessonFrom6Span(ArrayList<String> times, ArrayList<String> days, int i, int j, Elements elements) {
        Lesson.Builder builder1 = Lesson.newBuilder();
        Lesson.Builder builder2 = Lesson.newBuilder();

        this.lessons.add(builder1.setCurrentPair(i, times.get(i))
                .setCurrentDay(days.get(j))
                .setName(elements.get(0).text(),
                        elements.get(0).attr("title"))
                .setPairType(elements.get(1).text(),
                        elements.get(1).attr("title"))
                .setauditoryNumber(elements.get(2).text())
                .setTeacherName(elements.get(3).text(),
                        elements.get(3).attr("title"))
                .setCurrentHalfGroup(1, "1")
                .build());

        this.lessons.add(builder2.setCurrentPair(i, times.get(i))
                .setCurrentDay(days.get(j))
                .setName(elements.get(0).getElementsByTag("span").get(0).text(),
                        elements.get(0).getElementsByTag("span").get(0).attr("title"))
                .setPairType(elements.get(1).text(),
                        elements.get(1).attr("title"))
                .setauditoryNumber(elements.get(4).text())
                .setTeacherName(elements.get(5).text(),
                        elements.get(5).attr("title"))
                .setCurrentHalfGroup(2, "2")
                .build());
    }

    private Lesson getLesson(ArrayList<String> times, ArrayList<String> days, int i, int j, Element element) {
        String group = "3";
        for (TextNode node: element.textNodes())
        {
            if (node.text().contains("/ "))
            {
                group = node.text().replaceAll("[{/ }]+", "");
            }
        }

        return Lesson.newBuilder()
                .setCurrentPair(i, times.get(i))
                .setCurrentDay(days.get(j))
                .setName(element.getElementsByTag("span").first().text(),
                        element.getElementsByTag("span").first().attr("title"))
                .setPairType(element.getElementsByTag("span").get(1).text(),
                        element.getElementsByTag("span").get(1).attr("title"))
                .setauditoryNumber(element.getElementsByClass("aud_number").text())
                .setTeacherName(element.getElementsByTag("span").last().text(),
                        element.getElementsByTag("span").last().attr("title"))
                .setCurrentHalfGroup(Integer.parseInt(group), group)
                .build();
    }
}
