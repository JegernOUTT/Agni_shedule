package com.example.hellb.agni.serializible.scheduleData;

/**
 * Created by hellb on 06.10.2015.
 */
public class Group {
    private Integer postData;
    private String groupName;

    public Group(Integer post, String group)
    {
        postData = post;
        groupName = group;
    }

    @Override
    public String toString() {
        return  groupName;
    }
}
