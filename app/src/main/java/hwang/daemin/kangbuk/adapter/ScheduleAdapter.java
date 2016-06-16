package hwang.daemin.kangbuk.adapter;

/**
 * Created by user on 2016-05-27.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.data.ColumnData;
import hwang.daemin.kangbuk.data.ScheduleData;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private List<ScheduleData> scheduleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvTime;
        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
        }
    }


    public ScheduleAdapter(List<ScheduleData> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_schedule, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ScheduleData schedule = scheduleList.get(position);
        holder.tvTitle.setText(schedule.getTitle());
        holder.tvTime.setText(schedule.getTime());

    }
    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}