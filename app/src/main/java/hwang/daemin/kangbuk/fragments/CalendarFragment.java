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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.data.CalendarData;
import hwang.daemin.kangbuk.event.BackKeyEvent;


/**
 * Created by user on 2016-06-14.
 */
public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView calender;
    private LinearLayout ll;
    private EditText etTitle;
    private Button mSendButton;
    private DatabaseReference mFirebaseDatabaseReference;
    private ProgressBar bar;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mMessageRecyclerView;
    private FirebaseRecyclerAdapter<CalendarData, CalendarViewHolder>
            mFirebaseAdapter;
    private int year, month, day, screenMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_calendar));
        calender = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        calender.setOnDateChangedListener(this);
        calender.setOnMonthChangedListener(this);
        year = calender.getCurrentDate().getYear();
        month = calender.getCurrentDate().getMonth() + 1;
        day = calender.getCurrentDate().getDay();
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ll = (LinearLayout) rootView.findViewById(R.id.ll);
        etTitle = (EditText) rootView.findViewById(R.id.etTitle);
        mMessageRecyclerView = (RecyclerView) rootView.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mFirebaseDatabaseReference.child(year + "/" + month).orderByChild("starCount");
        mFirebaseAdapter = new FirebaseRecyclerAdapter<CalendarData,
                CalendarViewHolder>(
                CalendarData.class,
                R.layout.listitem_calendar,
                CalendarViewHolder.class,
                mFirebaseDatabaseReference.child(year + "/" + month).orderByChild("day")) {
            @Override
            protected void populateViewHolder(CalendarViewHolder viewHolder,
                                              CalendarData calendarData, int position) {
                viewHolder.tvDate.setText(calendarData.getMonth() + "." + calendarData.getDay());
                viewHolder.tvTitle.setText(calendarData.getTitle());
                bar.setVisibility(ProgressBar.GONE);

            }
        };
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
                        year, month, day, etTitle.getText().toString());
                mFirebaseDatabaseReference.child(year + "/" + month).push().setValue(calendarData);
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
            mFirebaseAdapter = new FirebaseRecyclerAdapter<CalendarData,
                    CalendarViewHolder>(
                    CalendarData.class,
                    R.layout.listitem_calendar,
                    CalendarViewHolder.class,
                    mFirebaseDatabaseReference.child(year + "/" + month).orderByChild("day")) {
                @Override
                protected void populateViewHolder(CalendarViewHolder viewHolder,
                                                  CalendarData calendarData, int position) {
                    viewHolder.tvDate.setText(calendarData.getMonth() + "." + calendarData.getDay());
                    viewHolder.tvTitle.setText(calendarData.getTitle());
                    bar.setVisibility(ProgressBar.GONE);
                }
            };
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
            screenMonth = month;
            mFirebaseAdapter = new FirebaseRecyclerAdapter<CalendarData,
                    CalendarViewHolder>(
                    CalendarData.class,
                    R.layout.listitem_calendar,
                    CalendarViewHolder.class,
                    mFirebaseDatabaseReference.child(year + "/" + month).orderByChild("day")) {
                @Override
                protected void populateViewHolder(CalendarViewHolder viewHolder,
                                                  CalendarData calendarData, int position) {
                    viewHolder.tvDate.setText(calendarData.getMonth() + "." + calendarData.getDay());
                    viewHolder.tvTitle.setText(calendarData.getTitle());
                    bar.setVisibility(ProgressBar.GONE);
                }
            };
            mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        }
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvTitle;

        public CalendarViewHolder(View v) {
            super(v);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
