package com.example.hellb.agni.connection;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.hellb.agni.serializible.SerializableData;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hellb on 06.10.2015.
 */
public class AgniWebConnectionForSchedule implements FutureCallback<InputStream> {
    private String sheduleUrl = "http://is.agni-rt.ru:8080/schedule/";
    private Context context;
    private SerializableData serializableData;

    public AgniWebConnectionForSchedule(Context con)
    {
        serializableData = SerializableData.getInstance();
        context = con;
        Ion.with(con)
            .load(sheduleUrl)
            .asInputStream()
            .setCallback(this);
    }

    @Override
    public void onCompleted(Exception e, InputStream result) {
        if (result != null)
        {
            Document document = Jsoup.parse(toStr(result));
            Elements text = document.getElementsByClass("text");
            Elements keys = text.get(1).getElementsByTag("a");

            for (Element link : keys) {
                String str = link.attr("href");
                int start = str.indexOf("faculty_id.value='") + 18;
                int end = str.indexOf("';", start);

                serializableData.getScheduleInputParams().put(Integer.parseInt(str.substring(start, end)), link.text());
            }
            serializableData.isRegisterParamReady = true;
        }
    }

    @Nullable
    private String toStr(InputStream stream)
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
