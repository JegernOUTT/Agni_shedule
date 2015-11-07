package com.example.hellb.agni.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
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

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public TextView textViewTime, textViewLessonName, textViewAuditoryName, textViewLessonType,
                textViewHalfGroup, textViewTeachName;
        private CardView clickableCard;
        private int originalHeight = 0;
        private boolean isViewExpanded = false;
        private LinearLayout yourCustomView;

        public ScheduleViewHolder(View itemView) {
            super(itemView);

            textViewTime = (TextView) itemView.findViewById(R.id.tvTime);
            textViewLessonName = (TextView) itemView.findViewById(R.id.tvLessonName);
            textViewAuditoryName = (TextView) itemView.findViewById(R.id.tvAuditoryName);
            textViewLessonType = (TextView) itemView.findViewById(R.id.tvLessonType);
            textViewHalfGroup = (TextView) itemView.findViewById(R.id.tvHalfGroup);
            textViewTeachName = (TextView) itemView.findViewById(R.id.tvTeachName);
            clickableCard = (CardView) itemView.findViewById(R.id.cardView);
            yourCustomView = (LinearLayout) itemView.findViewById(R.id.hiddenLayout);

            clickableCard.setClickable(true);
            clickableCard.setOnClickListener(this);

            if (isViewExpanded == false) {
                // Set Views to View.GONE and .setEnabled(false)
                yourCustomView.setVisibility(View.GONE);
                yourCustomView.setEnabled(false);
            }
        }

        @Override
        public void onClick(final View view) {
            if (originalHeight == 0) {
                originalHeight = view.getHeight();
            }

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;
            if (!isViewExpanded) {
                yourCustomView.setVisibility(View.VISIBLE);
                yourCustomView.setEnabled(true);
                isViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 0.95)); // These values in this method can be changed to expand however much you like
            } else {
                isViewExpanded = false;
                valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 0.95), originalHeight);

                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                a.setDuration(200);
                // Set a listener to the animation and configure onAnimationEnd
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        yourCustomView.setVisibility(View.INVISIBLE);
                        yourCustomView.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                // Set the animation on the custom view
                yourCustomView.startAnimation(a);
            }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });

            valueAnimator.start();
        }
    }
}
