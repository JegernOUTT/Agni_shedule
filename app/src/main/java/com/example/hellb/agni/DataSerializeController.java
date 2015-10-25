package com.example.hellb.agni;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.scheduleData.Course;
import com.example.hellb.agni.serializible.scheduleData.Faculty;
import com.example.hellb.agni.serializible.scheduleData.Group;
import com.example.hellb.agni.serializible.scheduleData.SerializableScheduleData;
import com.example.hellb.agni.serializible.scheduleData.Week;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

public class DataSerializeController extends Observable{
    private static Context context;
    private static String currentSettingsFileName = "currentSettings.dat";
    private static String modelFileName = "model.dat";

    private DataSerializeController(){

    };
    static DataSerializeController object = new DataSerializeController();
    public static synchronized DataSerializeController getInstance(Context context1){
        context = context1;
        return object;
    }

    public void deserializeSchedule(Observer observer){
        addObserver(observer);
    }

    public void serializeSchedule(Observer observer){
        addObserver(observer);
    }

    public void deserializeCurrentSettings(Observer observer){
        new AsyncTask<Observer, Void, Observer>(){
            @Override
            protected void onPostExecute(Observer aVoid) {
                super.onPostExecute(aVoid);
                aVoid.update(DataSerializeController.this, new String("deserializeCurrentSettings"));
            }

            @Override
            protected Observer doInBackground(Observer... params) {
                FileInputStream fis = null;
                ObjectInputStream is = null;
                try {
                    fis = context.openFileInput(currentSettingsFileName);
                    is = new ObjectInputStream(fis);
                    CurrentSettings.setInstance((CurrentSettings) is.readObject());
                    is.close();
                    fis.close();
                    implementRealModel();
                } catch (Exception exception) {
                    Log.e("Exception: ", exception.getMessage());
                    try {
                        is.close();
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return params[0];
                }
                return params[0];
            }

            private void implementRealModel() {
                for (Faculty faculty: SerializableScheduleData.getInstance().getFaculties())
                {
                    if (faculty.equals(CurrentSettings.getInstance().faculty))
                    {
                        CurrentSettings.getInstance().faculty = faculty;
                    }
                }

                for (Course course: CurrentSettings.getInstance().faculty.getCourses())
                {
                    if (course.equals(CurrentSettings.getInstance().course))
                    {
                        CurrentSettings.getInstance().course = course;
                    }
                }

                for (Group group: CurrentSettings.getInstance().course.getGroups())
                {
                    if (group.equals(CurrentSettings.getInstance().group))
                    {
                        CurrentSettings.getInstance().group = group;
                    }
                }

                for (Week week: CurrentSettings.getInstance().group.getWeeks())
                {
                    if (week.equals(CurrentSettings.getInstance().week))
                    {
                        CurrentSettings.getInstance().week = week;
                    }
                }



                CurrentSettings.getInstance().isLoaded = true;
            }

        }.execute(observer);
    }

    public void serializeCurrentSettings(Observer observer){
        new AsyncTask<Observer, Void, Observer>() {
            @Override
            protected void onPostExecute(Observer aVoid) {
                super.onPostExecute(aVoid);
                aVoid.update(DataSerializeController.this, new String("serializeCurrentSettings"));
            }

            @Override
            protected Observer doInBackground(Observer... params) {
                try
                {
                    FileOutputStream fos = context.openFileOutput(currentSettingsFileName, Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(CurrentSettings.getInstance());
                    os.close();
                    fos.close();
                }
                catch (Exception exception)
                {
                    Log.d("Exception: ", exception.getMessage());
                }
                return params[0];
            }
        }.execute(observer);
    }

    public void deserializeModel(Observer observer){
        new AsyncTask<Observer, Void, Observer>() {
            @Override
            protected Observer doInBackground(Observer... params) {
                FileInputStream fis = null;
                try {
                    fis = context.openFileInput(modelFileName);
                    ObjectInputStream is = new ObjectInputStream(fis);
                    SerializableScheduleData.setInstance((SerializableScheduleData) is.readObject());
                    is.close();
                    fis.close();
                } catch (Exception exception) {
                    Log.d("Exception in deser: ", exception.getMessage());
                }

                for (Faculty faculty: SerializableScheduleData.getInstance().getFaculties())
                {
                    for (Course course: faculty.getCourses())
                    {
                        course.owner = faculty;
                        for (Group group: course.getGroups())
                        {
                            group.owner = course;
                            for (Week week: group.getWeeks())
                            {
                                week.owner = group;
                                week.schedule.owner = week;
                            }
                        }
                    }
                }
                return params[0];
            }

            @Override
            protected void onPostExecute(Observer observer2) {
                super.onPostExecute(observer2);
                observer2.update(DataSerializeController.this, new String("deserializeModel"));
            }
        }.execute(observer);
    }

    public void serializeModel(Observer observer){

        new AsyncTask<Observer, Void, Observer>() {
            @Override
            protected void onPostExecute(Observer observer1) {
                super.onPostExecute(observer1);
                observer1.update(DataSerializeController.this, new String("serializeModel"));
            }

            @Override
            protected Observer doInBackground(Observer... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try
                {
                    FileOutputStream fos = context.openFileOutput(modelFileName, Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(SerializableScheduleData.getInstance());
                    os.close();
                    fos.close();
                }
                catch (Exception exception)
                {
                    Log.d("Exception: ", exception.getMessage());
                }
                return params[0];
            }
        }.execute(observer);
    }
}
