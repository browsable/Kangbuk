package hwang.daemin.kangbuk.fragments.picture;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import hwang.daemin.kangbuk.R;


public class PictureViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle;
    public TextView tvName;
    public TextView tvDate;
    public ImageView ivThumb;
    public ImageView ivOverflow;

    public PictureViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
        ivOverflow = (ImageView) itemView.findViewById(R.id.ivOverflow);
    }
}
