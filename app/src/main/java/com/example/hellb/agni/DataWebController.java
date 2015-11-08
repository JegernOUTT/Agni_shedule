package com.example.hellb.agni;


import android.content.Context;
import android.os.AsyncTask;

import com.example.hellb.agni.serializible.CurrentSettings;
import com.example.hellb.agni.serializible.scheduleData.SerializableScheduleData;

import java.util.Observable;
import java.util.Observer;

public class DataWebController extends Observable{
    private static Context context;

    private DataWebController(){

    };
    static DataWebController object = new DataWebController();
    public static synchronized DataWebController getInstance(Context context1){
        context = context1;
        return object;
    }

    public void downloadScheduleToCurrentSettings(final Observer observer) {
        DataGetStack.getInstance(1, context).
                addTask(CurrentSettings.getInstance().week.schedule);

        new AsyncTask<Observer, Void, Observer>() {
            @Override
            protected void onPostExecute(Observer aVoid) {
                super.onPostExecute(aVoid);
                observer.update(DataWebController.this, new String("downloadScheduleToCurrentSettings"));
            }

            @Override
            protected Observer doInBackground(Observer... params) {

                try {
                    Thread.sleep(1000);

                    while (!DataGetStack.getInstance().isReady() &&
                            !DataGetStack.getInstance().isConnectError())
                    {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return params[0];
            }
        }.execute(observer);
    }

    public void downloadModelToCurrentSettings(Observer observer) {
        DataGetStack.getInstance(20, context)
                .addTask(SerializableScheduleData.getInstance());

        new AsyncTask<Observer, Void, Observer>() {
            @Override
            protected void onPostExecute(Observer aVoid) {
                super.onPostExecute(aVoid);
                aVoid.update(DataWebController.this, new String("downloadModelToCurrentSettings"));
            }

            @Override
            protected Observer doInBackground(Observer... params) {
                while (true)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (DataGetStack.getInstance().isReady() ||
                            DataGetStack.getInstance().isConnectError())
                    {
                        SerializableScheduleData serializableScheduleData = SerializableScheduleData.getInstance();
                        serializableScheduleData.isLoaded();
                        break;
                    }
                }
                return params[0];
            }
        }.execute(observer);
    }
}
