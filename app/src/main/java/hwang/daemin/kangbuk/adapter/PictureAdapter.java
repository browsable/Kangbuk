package hwang.daemin.kangbuk.adapter;

/**
 * Created by user on 2016-05-27.
 */

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.data.PictureData;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private Context mContext;
    private List<PictureData> picList;

    public class PictureViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvName;
        public ImageView ivThumb;
        public ImageView ivOverflow;

        public PictureViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            ivOverflow = (ImageView) itemView.findViewById(R.id.ivOverflow);
        }
    }


    public PictureAdapter(Context mContext, List<PictureData> picList) {
        this.mContext = mContext;
        this.picList = picList;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_column, parent, false);
        return new PictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PictureViewHolder holder, int position) {
        PictureData pic = picList.get(position);
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_picture, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    return true;
                case R.id.action_play_next:
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }
}