package hwang.daemin.kangbuk.fragments.week;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.CodelabPreferences;
import hwang.daemin.kangbuk.common.GlideUtil;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.data.GroupData;
import hwang.daemin.kangbuk.firebase.FUtil;
import hwang.daemin.kangbuk.main.UserDetailActivity;


/**
 * Created by user on 2016-06-14.
 */
public class WeekGroupFragment extends Fragment {
    public static final String WEEK_GROUP = "weekgroup";
    private Button mSendButton;
    private EditText mMessageEditText;
    private SharedPreferences mSharedPreferences;
    private StorageReference mStoreRefThumb;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 30;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mMessageRecyclerView;
    private FirebaseRecyclerAdapter<GroupData, MessageViewHolder>
            mFirebaseAdapter;
    private String tmpThumbPhotoURL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_group, container, false);
        My.INFO.backKeyName = "";
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_week_group));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMessageRecyclerView = (RecyclerView) rootView.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        // New child entries
        mFirebaseAdapter = new FirebaseRecyclerAdapter<GroupData,
                MessageViewHolder>(
                GroupData.class,
                R.layout.listitem_message,
                MessageViewHolder.class,
                FUtil.databaseReference.child(WEEK_GROUP)) {

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              GroupData groupData, int position) {
                final MessageViewHolder viewHol = viewHolder;
                viewHol.messageTextView.setText(groupData.getText());
                viewHol.messengerTextView.setText(groupData.getName());
                final String uId = groupData.getuId();
                FUtil.databaseReference.child("user/" + uId + "/thumbPhotoURL/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tmpThumbPhotoURL = (String) dataSnapshot.getValue();
                        GlideUtil.loadProfileIcon(tmpThumbPhotoURL, viewHol.messengerImageView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                if(FUtil.getCurrentUserId().equals(uId)) {
                    viewHolder.btRemove.setVisibility(View.VISIBLE);
                    viewHolder.btRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FUtil.databaseReference.child("weekgroup/" + uId).removeValue();
                        }
                    });
                }
                viewHol.messengerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), UserDetailActivity.class);
                        i.putExtra("uId",uId);
                        startActivity(i);
                    }
                });
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int groupDataCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (groupDataCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText = (EditText) rootView.findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
                .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) rootView.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send messages on click.
                String uId = FUtil.getCurrentUserId();
                GroupData groupData = new GroupData(mMessageEditText.getText().toString(),
                        FUtil.getCurrentUserName(), uId);
                FUtil.databaseReference.child(WEEK_GROUP).child(FUtil.getCurrentUserId()).setValue(groupData);
                mMessageEditText.setText("");
            }
        });

        return rootView;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public CircleImageView messengerImageView;
        public Button btRemove;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            btRemove = (Button) itemView.findViewById(R.id.btRemove);
        }
    }


}
