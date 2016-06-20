package hwang.daemin.kangbuk.fragments.file;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;


/**
 * Created by user on 2016-06-14.
 */
public class MP3Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mp3,container,false);
        My.INFO.backKeyName ="";

        // Button launches NewPostActivity
        rootView.findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return rootView;
    }
}
