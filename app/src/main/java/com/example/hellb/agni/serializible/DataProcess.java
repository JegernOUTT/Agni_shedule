package com.example.hellb.agni.serializible;

import android.content.Context;

import java.util.Observer;

/**
 * Created by hellb on 07.10.2015.
 */
public interface DataProcess {
    void processData(Context context, Observer observer);
}
