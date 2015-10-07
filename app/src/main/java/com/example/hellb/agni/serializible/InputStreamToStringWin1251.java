package com.example.hellb.agni.serializible;

import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hellb on 07.10.2015.
 */
public class InputStreamToStringWin1251 {

    @Nullable
    public static String toStr(InputStream stream)
    {
        String file = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream,
                    "windows-1251"), 8192);
            String str;
            while ((str = br.readLine()) != null) {
                file += str;
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
