package hwang.daemin.kangbuk.fragments.picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.GlideUtil;
import hwang.daemin.kangbuk.data.Comment;
import hwang.daemin.kangbuk.data.PictureData;
import hwang.daemin.kangbuk.data.User;
import hwang.daemin.kangbuk.firebase.fUtil;
import hwang.daemin.kangbuk.main.UserDetailActivity;

public class PictureDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PictureDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_UID = "uId";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private TextView tvDate;
    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;
    private RecyclerView photoRecycler;
    private ScrollView sv;
    private String date;
    private ImageView ivProfile;
    private ImageView ivOverflow;
    FirebaseRecyclerAdapter<String,
            PhotoViewHolder> mFirebaseAdapter;
    String thumbPhotoURL,uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sv = (ScrollView) findViewById(R.id.sv);
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        date =  getIntent().getStringExtra(EXTRA_DATE);
        uId = getIntent().getStringExtra(EXTRA_UID);

        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = fUtil.databaseReference.child("picture").child(mPostKey);
        mCommentsReference = fUtil.databaseReference.child("picture-comments").child(mPostKey);

        // Initialize Views
        tvDate = (TextView) findViewById(R.id.tvDate);
        mAuthorView = (TextView) findViewById(R.id.post_author);
        mTitleView = (TextView) findViewById(R.id.post_title);
        mBodyView = (TextView) findViewById(R.id.post_body);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ivOverflow = (ImageView) findViewById(R.id.ivOverflow);
        fUtil.databaseReference.child("user/" + uId + "/thumbPhotoURL/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thumbPhotoURL = (String) dataSnapshot.getValue();
                try {
                    Glide.with(PictureDetailActivity.this)
                            .load(thumbPhotoURL)
                            .placeholder(R.drawable.ic_account_circle_black_36dp)
                            .dontAnimate()
                            .fitCenter()
                            .into(ivProfile);
                }catch (IllegalArgumentException e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PictureDetailActivity.this, UserDetailActivity.class);
                i.putExtra("uId",uId);
                startActivity(i);
            }
        });
        ivOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fUtil.getCurrentUserId().equals(uId)) {
                    showPopupMenu(ivOverflow, 0);
                }
            }
        });
        photoRecycler = (RecyclerView) findViewById(R.id.recycler_photo);
        photoRecycler.setLayoutManager(new LinearLayoutManager(this));
        tvDate.setText(date);

        mFirebaseAdapter = new FirebaseRecyclerAdapter<String,
                PhotoViewHolder>(
                String.class,
                R.layout.listitem_picturedetail,
                PhotoViewHolder.class,
                fUtil.databaseReference.child("picture-urls").child(mPostKey)){

            @Override
            protected void populateViewHolder(final PhotoViewHolder viewHolder,
                                              String fullURL, int position) {
                GlideUtil.loadImage(fullURL, viewHolder.ivPhoto);
            }
        };
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mFirebaseAdapter.notifyDataSetChanged();
            }
        });
        photoRecycler.setAdapter(mFirebaseAdapter);
        photoRecycler.setNestedScrollingEnabled(false);
        mCommentField = (EditText) findViewById(R.id.field_comment_text);
        mCommentButton = (Button) findViewById(R.id.button_post_comment);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);
        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCommentsRecycler.setNestedScrollingEnabled(false);

    }
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;
        public PhotoViewHolder(View v) {
            super(v);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                PictureData pic = dataSnapshot.getValue(PictureData.class);
                mAuthorView.setText(pic.uName);
                mTitleView.setText(pic.title);
                mBodyView.setText(pic.body);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(PictureDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mPostReference.addValueEventListener(postListener);

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
        // Listen for comments
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_post_comment:
                if (TextUtils.isEmpty(mCommentField.getText().toString())) {
                    mCommentField.setError("댓글을 입력하세요");
                    return;
                }
                postComment();
                break;
        }
    }

    private void postComment() {
        final String uid = fUtil.getCurrentUserId();
        fUtil.databaseReference.child("user").child(uid)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String authorName = user.getuName();
                    Long now = System.currentTimeMillis();
                    String commentText = mCommentField.getText().toString();
                    Comment comment = new Comment(uid, authorName, commentText,now);
                    mCommentsReference.child(now.toString()).setValue(comment);
                    mCommentField.setText(null);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;
        public TextView tvDate;
        public ImageView ivProfile;
        public ImageView ivOverflow;

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.comment_author);
            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            ivOverflow = (ImageView) itemView.findViewById(R.id.ivOverflow);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    // Update RecyclerView
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Replace with the new data
                        mComments.set(commentIndex, newComment);

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();


                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.listitem_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            final CommentViewHolder viewHolder = holder;
            final Comment comment = mComments.get(position);
            viewHolder.authorView.setText(comment.author);
            viewHolder.bodyView.setText(comment.text);
            viewHolder.tvDate.setText(DateUtils.getRelativeTimeSpanString(comment.time));
            final String comentId = comment.uid;
            fUtil.databaseReference.child("user/" + comentId + "/thumbPhotoURL/").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    thumbPhotoURL = (String) dataSnapshot.getValue();
                    try{
                    Glide.with(PictureDetailActivity.this)
                            .load(thumbPhotoURL)
                            .placeholder(R.drawable.ic_account_circle_black_36dp)
                            .dontAnimate()
                            .fitCenter()
                            .into(viewHolder.ivProfile);
                }catch (IllegalArgumentException e){
                        e.printStackTrace();
                }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            if(fUtil.getCurrentUserId().equals(comentId)) {
                viewHolder.ivOverflow.setVisibility(View.VISIBLE);
                viewHolder.ivOverflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu(viewHolder.ivOverflow, comment.time);
                    }
                });
            }
            viewHolder.ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PictureDetailActivity.this, UserDetailActivity.class);
                    i.putExtra("uId",comentId);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
    private void showPopupMenu(View view, long time) {
        // inflate menu
        PopupMenu popup = new PopupMenu(PictureDetailActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_picture, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(time));
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Long time;
        public MyMenuItemClickListener(Long time) {
            this.time = time;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_remove:
                    if(time==0) {
                        if (mPostListener != null) {
                            mPostReference.removeEventListener(mPostListener);
                        }
                        // Clean up comments listener
                        mAdapter.cleanupListener();
                        finish();
                        fUtil.databaseReference.child("picture/" + mPostKey).removeValue();
                        fUtil.databaseReference.child("picture-comments/" + mPostKey).removeValue();
                        fUtil.databaseReference.child("picture-urls/" + mPostKey).removeValue();
                        fUtil.getStorePictureRef().child(mPostKey).delete();
                        return true;
                    }else{
                        fUtil.databaseReference.child("picture-comments/" + mPostKey+"/"+time.toString()).removeValue();
                    }
                default:
            }
            return false;
        }
    }
}
