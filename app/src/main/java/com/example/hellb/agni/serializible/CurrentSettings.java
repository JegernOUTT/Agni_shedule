package com.example.hellb.agni.serializible;

/**
 * Created by hellb on 07.10.2015.
 */
public class CurrentSettings {
    private static CurrentSettings ourInstance = new CurrentSettings();

    public static CurrentSettings getInstance() {
        return ourInstance;
    }

    private CurrentSettings() {
    }
}
