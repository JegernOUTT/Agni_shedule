package com.example.hellb.agni.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellb.agni.R;
import com.example.hellb.agni.serializible.scheduleData.Lesson;
import com.example.hellb.agni.serializible.scheduleEnums.HalfGroup;

import java.util.List;

/**
 * Created by hellb on 17.10.2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private Context context;
    private List<Lesson> lessons;
    private LayoutInflater layoutInflater;

    public ScheduleAdapter(Context con, List<Lesson> lessonList) {
        context = con;
        lessons = lessonList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_adapter_view, parent, false);
        ScheduleViewHolder pvh = new ScheduleViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        holder.textViewTime.setText(((Lesson) getItem(position)).getCurrentPair().first);
        if (((Lesson) getItem(position)).getName().second.length() > 42)
        {
            String tmpString = ((Lesson) getItem(position)).getName().second.substring(0, 39);
            tmpString += "...";
            holder.textViewLessonName.setText(tmpString);
        }
        else
        {
            holder.textViewLessonName.setText(((Lesson) getItem(position)).getName().second);
        }
        holder.textViewAuditoryName.setText(((Lesson) getItem(position)).getAuditoryNumber());
        holder.textViewLessonType.setText(((Lesson) getItem(position)).getPairType().second);
        if (!(((Lesson) getItem(position)).getCurrentHalfGroup().second == HalfGroup.COMMON_HALF_GROUP))
        {
            holder.textViewHalfGroup.setText("Подгруппа: " + ((Lesson) getItem(position)).getCurrentHalfGroup().first);
        }
        else {holder.textViewHalfGroup.setText("");}
        holder.textViewTeachName.setText(((Lesson) getItem(position)).getTeacherName().second);
    }

    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTime, textViewLessonName, textViewAuditoryName, textViewLessonType,
                textViewHalfGroup, textViewTeachName;

        public ScheduleViewHolder(View itemView) {
            super(itemView);

            textViewTime = (TextView) itemView.findViewById(R.id.tvTime);
            textViewLessonName = (TextView) itemView.findViewById(R.id.tvLessonName);
            textViewAuditoryName = (TextView) itemView.findViewById(R.id.tvAuditoryName);
            textViewLessonType = (TextView) itemView.findViewById(R.id.tvLessonType);
            textViewHalfGroup = (TextView) itemView.findViewById(R.id.tvHalfGroup);
            textViewTeachName = (TextView) itemView.findViewById(R.id.tvTeachName);
        }
    }
}
