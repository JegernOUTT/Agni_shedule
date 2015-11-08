package com.example.hellb.agni;

import android.content.Context;

import com.example.hellb.agni.serializible.DataProcess;
import com.example.hellb.agni.serializible.scheduleData.SerializableScheduleData;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by hellb on 08.10.2015.
 */

public class DataGetStack implements Observer, Runnable {
    private LinkedList<DataProcess> dataProcessStack;
    private volatile int connectionCount;
    private volatile int currentConnection;
    private static DataGetStack instance;
    private Context context;
    private boolean isConnectionError;

    public static DataGetStack getInstance(int connections, Context context1) {
        if (instance == null)
        {
            instance = new DataGetStack(connections, context1);
        }
        return instance;
    }

    public LinkedList<DataProcess> getDataProcessStack() {
        return dataProcessStack;
    }

    public boolean isConnectError() {
        if (isConnectionError)
        {
            isConnectionError = false;
            return true;
        }
        return false;
    }

    public static DataGetStack getInstance() {
        return instance;
    }

    private DataGetStack(int connections, Context context1) {
        context = context1;
        connectionCount = connections;
        isConnectionError = false;
        dataProcessStack = new LinkedList<DataProcess>();

        new Thread(this).start();
    }

    public void clearStack() {
        dataProcessStack.clear();
        currentConnection = 0;
        isConnectionError = true;
    }

    public void addTask(DataProcess dataProcess)
    {
        dataProcessStack.addLast(dataProcess);
    }

    public boolean isReady() {
        if (SerializableScheduleData.getInstance().isLoaded() && dataProcessStack.size() == 0
                && currentConnection == 0)
        {
            return true;
        }
        else return false;
    }

    @Override
    public void update(Observable observable, Object data) {
        //dataProcessStack.remove(data);
        if (currentConnection > 0)
        {
            --currentConnection;
        }
    }

    @Override
    public void run() {
        while (true)
        {
            if (currentConnection < connectionCount)
            {
                if (dataProcessStack.size() > 0)
                {
                    dataProcessStack.pollFirst().processData(context, this);
                    ++currentConnection;
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
