package hwang.daemin.kangbuk.fragments.file;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.firebase.fUtil;

/**
 * @author msahakyan
 */
public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YoutubeVideoViewHolder> {

    private final ThumbnailListener mThumbnailListener;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> mThumbnailViewToLoaderMap;

    private List<YoutubeVideo> mYoutubeVideos;
    private Context mContext;

    public YoutubeVideoAdapter(Context context, List<YoutubeVideo> youtubeVideos) {
        mContext = context;
        mYoutubeVideos = youtubeVideos;
        mThumbnailListener = new ThumbnailListener();
        mThumbnailViewToLoaderMap = new HashMap<>();
    }

    @Override
    public YoutubeVideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_video, null, false);
        return new YoutubeVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final YoutubeVideoViewHolder holder, final int position) {
        final YoutubeVideo youtubeVideo = mYoutubeVideos.get(position);

        YouTubeThumbnailView videoThumb = holder.youTubeThumbnailView;
        YouTubeThumbnailLoader loader = mThumbnailViewToLoaderMap.get(videoThumb);
        if (loader == null) {
            videoThumb.setTag(youtubeVideo.getVideoId());
            videoThumb.initialize(DeveloperKey.DEVELOPER_KEY, mThumbnailListener);
        } else {
            videoThumb.setImageResource(R.drawable.loading_thumbnail);
            loader.setVideo(youtubeVideo.getVideoId());
        }
        holder.youtubeVideoTitle.setText(youtubeVideo.getTitle());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                openYoutubePlayer(youtubeVideo);
            }
        });
        if(fUtil.getCurrentUserId().equals(youtubeVideo.getuId())) {
            holder.btRemove.setVisibility(View.VISIBLE);
            holder.btRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fUtil.databaseReference.orderByChild("uId").equalTo(youtubeVideo.getuId()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Log.i("test","onChildAdded");
                            fUtil.databaseReference.child("youtube").child(dataSnapshot.getKey()).removeValue();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            Log.i("test","onChildChanged");
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            Log.i("test","onChildRemoved");
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            Log.i("test","onChildMoved");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("test","onCancelled");
                        }
                    });
                }
            });
        }
    }

    public void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : mThumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    private void openYoutubePlayer(YoutubeVideo video) {
        Activity activity = (Activity) mContext;
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity,
                DeveloperKey.DEVELOPER_KEY, video.getVideoId(), 0, true, false);
        try {
            activity.startActivity(intent);
            releaseLoaders();
        } catch (ActivityNotFoundException e) {
            // Could not resolve the intent - must need to install or update the YouTube API service.
            YouTubeInitializationResult.SERVICE_MISSING
                    .getErrorDialog(activity,0).show();
        }
    }

    @Override
    public int getItemCount() {
        return mYoutubeVideos.size();
    }

    static class YoutubeVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        private ItemClickListener clickListener;
        YouTubeThumbnailView youTubeThumbnailView;
        TextView youtubeVideoTitle;
        ImageView youtubeVideoPlayBtn;
        Button btRemove;
        YoutubeVideoViewHolder(View view) {
            super(view);
            youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtube_thumbnail_view);
            youtubeVideoTitle = (TextView) view.findViewById(R.id.youtube_video_title);
            youtubeVideoPlayBtn = (ImageView) view.findViewById(R.id.video_play_button);
            btRemove = (Button) view.findViewById(R.id.btRemove);
            view.setOnTouchListener(this);
            view.setOnClickListener(this);
        }

        void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.clickListener.onClick(v, getPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    scaleView(youtubeVideoPlayBtn, 1.3f);
                    break;
                case MotionEvent.ACTION_UP:
                    scaleView(youtubeVideoPlayBtn, 1.0f);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    scaleView(youtubeVideoPlayBtn, 1.0f);
                    break;
            }
            return false;
        }

        private void scaleView(View view, float scaleValue) {
            view.clearAnimation();
            view.animate()
                    .scaleX(scaleValue)
                    .scaleY(scaleValue)
                    .setDuration(100)
                    .setDuration(300);
        }
    }

    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            view.setImageResource(R.drawable.loading_thumbnail);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
            mThumbnailViewToLoaderMap.put(view, loader);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }

    private interface ItemClickListener {
        void onClick(View view, int position);
    }
}