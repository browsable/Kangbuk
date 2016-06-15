package hwang.daemin.kangbuk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.event.BackKeyEvent;


/**
 * Created by user on 2016-06-14.
 */
public class PlaceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_place,container,false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_place));
        return rootView;
    }
}
