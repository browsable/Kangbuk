package hwang.daemin.kangbuk.adapter;

/**
 * Created by user on 2016-05-27.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.data.ColumnData;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.MyViewHolder> {

    private List<ColumnData.Product> columnList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvBibleContent;
        public TextView tvDate;
        public TextView tvNum;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvBibleContent = (TextView) view.findViewById(R.id.tvBibleContent);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvNum = (TextView) view.findViewById(R.id.tvNum);
        }
    }


    public ColumnAdapter(List<ColumnData.Product> columnList) {
        this.columnList = columnList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_column, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ColumnData.Product column = columnList.get(position);
        holder.tvNum.setText(column.getNum());
        holder.tvTitle.setText(column.getTitle());
        holder.tvBibleContent.setText(column.getBible_content());
        holder.tvDate.setText(column.getDate());

    }

    @Override
    public int getItemCount() {
        return columnList.size();
    }
}