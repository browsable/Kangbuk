package hwang.daemin.kangbuk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.data.CalendarData;
import hwang.daemin.kangbuk.firebase.FUtil;


/**
 * Created by user on 2016-06-14.
 */
public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView calender;
    private LinearLayout ll;
    private EditText etTitle;
    private Button mSendButton;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mMessageRecyclerView;
    private FirebaseRecyclerAdapter<CalendarData, CalendarViewHolder>
            mFirebaseAdapter;
    private int year, month, day, screenMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        My.INFO.backKeyName ="";
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_calendar));
        calender = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        calender.setOnDateChangedListener(this);
        calender.setOnMonthChangedListener(this);
        year = calender.getCurrentDate().getYear();
        month = calender.getCurrentDate().getMonth() + 1;
        day = calender.getCurrentDate().getDay();
        ll = (LinearLayout) rootView.findViewById(R.id.ll);
        etTitle = (EditText) rootView.findViewById(R.id.etTitle);
        mMessageRecyclerView = (RecyclerView) rootView.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        // New child entries
        Query allPostsQuery = FUtil.databaseReference.child(year + "/" + month).orderByChild("day");
        mFirebaseAdapter = getFirebaseRecyclerAdapter(allPostsQuery);
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int CalendarDataCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (CalendarDataCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        etTitle.addTextChangedListener(new TextWatcher() {
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
                CalendarData calendarData = new CalendarData(
                        year, month, day, etTitle.getText().toString(), FUtil.getCurrentUserId());
                FUtil.databaseReference.child(year + "/" + month).push().setValue(calendarData);
                etTitle.setText("");
                mFirebaseAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        ll.setVisibility(View.VISIBLE);
        year = date.getYear();
        month = date.getMonth() + 1;
        day = date.getDay();
        mSendButton.setText(month + "월" + day + "일 " + "일정추가");
        if (screenMonth != month) {
            screenMonth = month;
            Query allPostsQuery = FUtil.databaseReference.child(year + "/" + month).orderByChild("day");
            mFirebaseAdapter = getFirebaseRecyclerAdapter(allPostsQuery);
            mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        ll.setVisibility(View.VISIBLE);
        year = date.getYear();
        month = date.getMonth() + 1;
        day = date.getDay();
        mSendButton.setText(month + "월" + day + "일 " + "일정추가");
        if (screenMonth != month) {
            ll.setVisibility(View.INVISIBLE);
            screenMonth = month;
            Query allPostsQuery = FUtil.databaseReference.child(year + "/" + month).orderByChild("day");
            mFirebaseAdapter = getFirebaseRecyclerAdapter(allPostsQuery);
            mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        }
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvTitle;
        public Button btRemove;

        public CalendarViewHolder(View v) {
            super(v);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            btRemove = (Button) itemView.findViewById(R.id.btRemove);
        }
    }

    private FirebaseRecyclerAdapter<CalendarData, CalendarViewHolder> getFirebaseRecyclerAdapter(Query query) {
        return new FirebaseRecyclerAdapter<CalendarData, CalendarViewHolder>(
                CalendarData.class, R.layout.listitem_calendar, CalendarViewHolder.class, query) {

            @Override
            public void populateViewHolder(final CalendarViewHolder viewHolder,
                                           final CalendarData calendarData, final int position) {
                final String key = this.getRef(position).getKey();
                viewHolder.tvDate.setText(calendarData.getMonth() + "." + calendarData.getDay());
                viewHolder.tvTitle.setText(calendarData.getTitle());
                if(FUtil.getCurrentUserId().equals(calendarData.getuId())) {
                    viewHolder.btRemove.setVisibility(View.VISIBLE);
                    viewHolder.btRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           FUtil.databaseReference.child(String.valueOf(calendarData.getYear()))
                                   .child(String.valueOf(calendarData.getMonth())).child(key).removeValue();
                        }
                    });
                }
            }

            @Override
            public void onViewRecycled(CalendarViewHolder holder) {
                super.onViewRecycled(holder);
//                FUtil.getLikesRef().child(holder.mPostKey).removeEventListener(holder.mLikeListener);
            }

        };
    }
}
