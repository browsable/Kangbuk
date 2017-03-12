package hwang.daemin.kangbuk.fragments.week;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.data.GroupData;
import hwang.daemin.kangbuk.firebase.fUtil;


/**
 * Created by user on 2016-06-14.
 */
public class WeekGroupFragment extends Fragment {
    private ProgressBar bar;
    public static final String WEEK_GROUP = "weekgroup2";
    LinearLayout btSave;
    EditText[][] editText;
    GroupData groupData;
    String packageName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_group, container, false);
        My.INFO.backKeyName = "";
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_week_group));
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        editText = new EditText[10][6];
        int resourceId;
        packageName = getActivity().getPackageName();
        for (int i=0; i<10; i++){
            for(int j=0; j<6; j++){
                resourceId = getResources().getIdentifier("etGroup"+i+"_"+j, "id", packageName);
                editText[i][j] = (EditText) rootView.findViewById(resourceId);
            }
        }
        try {
            fUtil.databaseReference.child(WEEK_GROUP).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GroupData groupData = dataSnapshot.getValue(GroupData.class);
                    if(groupData!=null) {
                        editText[0][0].setText(groupData.getEtGroup0_0());
                        editText[0][1].setText(groupData.getEtGroup0_1());
                        editText[0][2].setText(groupData.getEtGroup0_2());
                        editText[0][3].setText(groupData.getEtGroup0_3());
                        editText[0][4].setText(groupData.getEtGroup0_4());
                        editText[0][5].setText(groupData.getEtGroup0_5());
                        editText[1][0].setText(groupData.getEtGroup1_0());
                        editText[1][1].setText(groupData.getEtGroup1_1());
                        editText[1][2].setText(groupData.getEtGroup1_2());
                        editText[1][3].setText(groupData.getEtGroup1_3());
                        editText[1][4].setText(groupData.getEtGroup1_4());
                        editText[1][5].setText(groupData.getEtGroup1_5());
                        editText[2][0].setText(groupData.getEtGroup2_0());
                        editText[2][1].setText(groupData.getEtGroup2_1());
                        editText[2][2].setText(groupData.getEtGroup2_2());
                        editText[2][3].setText(groupData.getEtGroup2_3());
                        editText[2][4].setText(groupData.getEtGroup2_4());
                        editText[2][5].setText(groupData.getEtGroup2_5());
                        editText[3][0].setText(groupData.getEtGroup3_0());
                        editText[3][1].setText(groupData.getEtGroup3_1());
                        editText[3][2].setText(groupData.getEtGroup3_2());
                        editText[3][3].setText(groupData.getEtGroup3_3());
                        editText[3][4].setText(groupData.getEtGroup3_4());
                        editText[3][5].setText(groupData.getEtGroup3_5());
                        editText[4][0].setText(groupData.getEtGroup4_0());
                        editText[4][1].setText(groupData.getEtGroup4_1());
                        editText[4][2].setText(groupData.getEtGroup4_2());
                        editText[4][3].setText(groupData.getEtGroup4_3());
                        editText[4][4].setText(groupData.getEtGroup4_4());
                        editText[4][5].setText(groupData.getEtGroup4_5());
                        editText[5][0].setText(groupData.getEtGroup5_0());
                        editText[5][1].setText(groupData.getEtGroup5_1());
                        editText[5][2].setText(groupData.getEtGroup5_2());
                        editText[5][3].setText(groupData.getEtGroup5_3());
                        editText[5][4].setText(groupData.getEtGroup5_4());
                        editText[5][5].setText(groupData.getEtGroup5_5());
                        editText[6][0].setText(groupData.getEtGroup6_0());
                        editText[6][1].setText(groupData.getEtGroup6_1());
                        editText[6][2].setText(groupData.getEtGroup6_2());
                        editText[6][3].setText(groupData.getEtGroup6_3());
                        editText[6][4].setText(groupData.getEtGroup6_4());
                        editText[6][5].setText(groupData.getEtGroup6_5());
                        editText[7][0].setText(groupData.getEtGroup7_0());
                        editText[7][1].setText(groupData.getEtGroup7_1());
                        editText[7][2].setText(groupData.getEtGroup7_2());
                        editText[7][3].setText(groupData.getEtGroup7_3());
                        editText[7][4].setText(groupData.getEtGroup7_4());
                        editText[7][5].setText(groupData.getEtGroup7_5());
                        editText[8][0].setText(groupData.getEtGroup8_0());
                        editText[8][1].setText(groupData.getEtGroup8_1());
                        editText[8][2].setText(groupData.getEtGroup8_2());
                        editText[8][3].setText(groupData.getEtGroup8_3());
                        editText[8][4].setText(groupData.getEtGroup8_4());
                        editText[8][5].setText(groupData.getEtGroup8_5());
                        editText[9][0].setText(groupData.getEtGroup9_0());
                        editText[9][1].setText(groupData.getEtGroup9_1());
                        editText[9][2].setText(groupData.getEtGroup9_2());
                        editText[9][3].setText(groupData.getEtGroup9_3());
                        editText[9][4].setText(groupData.getEtGroup9_4());
                        editText[9][5].setText(groupData.getEtGroup9_5());
                    }
                    bar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        btSave = (LinearLayout) rootView.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                groupData = new GroupData(
                        editText[0][0].getText().toString(),editText[0][1].getText().toString(),editText[0][2].getText().toString(),editText[0][3].getText().toString(),editText[0][4].getText().toString(),editText[0][5].getText().toString(),
                        editText[1][0].getText().toString(),editText[1][1].getText().toString(),editText[1][2].getText().toString(),editText[1][3].getText().toString(),editText[1][4].getText().toString(),editText[1][5].getText().toString(),
                        editText[2][0].getText().toString(),editText[2][1].getText().toString(),editText[2][2].getText().toString(),editText[2][3].getText().toString(),editText[2][4].getText().toString(),editText[2][5].getText().toString(),
                        editText[3][0].getText().toString(),editText[3][1].getText().toString(),editText[3][2].getText().toString(),editText[3][3].getText().toString(),editText[3][4].getText().toString(),editText[3][5].getText().toString(),
                        editText[4][0].getText().toString(),editText[4][1].getText().toString(),editText[4][2].getText().toString(),editText[4][3].getText().toString(),editText[4][4].getText().toString(),editText[4][5].getText().toString(),
                        editText[5][0].getText().toString(),editText[5][1].getText().toString(),editText[5][2].getText().toString(),editText[5][3].getText().toString(),editText[5][4].getText().toString(),editText[5][5].getText().toString(),
                        editText[6][0].getText().toString(),editText[6][1].getText().toString(),editText[6][2].getText().toString(),editText[6][3].getText().toString(),editText[6][4].getText().toString(),editText[6][5].getText().toString(),
                        editText[7][0].getText().toString(),editText[7][1].getText().toString(),editText[7][2].getText().toString(),editText[7][3].getText().toString(),editText[7][4].getText().toString(),editText[7][5].getText().toString(),
                        editText[8][0].getText().toString(),editText[8][1].getText().toString(),editText[8][2].getText().toString(),editText[8][3].getText().toString(),editText[8][4].getText().toString(),editText[8][5].getText().toString(),
                        editText[9][0].getText().toString(),editText[9][1].getText().toString(),editText[9][2].getText().toString(),editText[9][3].getText().toString(),editText[9][4].getText().toString(),editText[9][5].getText().toString());

                fUtil.databaseReference.child(WEEK_GROUP).setValue(groupData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                    }
                });
            }
        });
        // New child entries
        return rootView;
    }

}
