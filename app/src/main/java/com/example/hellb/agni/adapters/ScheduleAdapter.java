package com.example.hellb.agni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.scheduleData.Lesson;

import java.util.List;

/**
 * Created by hellb on 17.10.2015.
 */
public class ScheduleAdapter extends BaseAdapter {
    private Context context;
    private List<Lesson> lessons;
    private LayoutInflater layoutInflater;

    public ScheduleAdapter(Context con, List<Lesson> lessonList) {
        context = con;
        lessons = lessonList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = (View) layoutInflater.inflate(R.layout.schedule_adapter_view, parent, false);
        }

        TextView textViewTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView textViewLessonName = (TextView) convertView.findViewById(R.id.tvLessonName);
        TextView textViewAuditoryName = (TextView) convertView.findViewById(R.id.tvAuditoryName);
        TextView textViewLessonType = (TextView) convertView.findViewById(R.id.tvLessonType);
        TextView textViewHalfGroup = (TextView) convertView.findViewById(R.id.tvHalfGroup);


        textViewTime.setText(((Lesson) getItem(position)).getCurrentPair().first);
        textViewLessonName.setText(((Lesson) getItem(position)).getName().second);
        textViewAuditoryName.setText(((Lesson) getItem(position)).getAuditoryNumber());
        textViewLessonType.setText(((Lesson) getItem(position)).getPairType().second);
        textViewHalfGroup.setText("Подгруппа: " + ((Lesson) getItem(position)).getCurrentHalfGroup().first);

        return convertView;
    }
}
