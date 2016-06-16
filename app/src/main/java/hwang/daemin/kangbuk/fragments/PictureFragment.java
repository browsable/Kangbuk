package hwang.daemin.kangbuk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.User;
import hwang.daemin.kangbuk.event.BackKeyEvent;


/**
 * Created by user on 2016-06-14.
 */
public class PictureFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_picture,container,false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_picture));

        return rootView;
    }
}
