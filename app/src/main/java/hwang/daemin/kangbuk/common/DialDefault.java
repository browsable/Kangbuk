package hwang.daemin.kangbuk.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import hwang.daemin.kangbuk.R;

/**
 * Created by ngn on 2016-06-24.
 */
public class DialDefault extends Dialog {
    private Button btDialCancel;
    private Button btDialSetting;
    private TextView tvTitle;
    private TextView tvContent;
    private String title, content;
    private int callFuncIndex;
    private Context context;
    private WindowManager.LayoutParams layoutParams;
    private DisplayMetrics dm;
    private Window window;
    public DialDefault(Context context, String title, String content, int callFuncIndex) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context=context;
        this.title=title;
        this.content=content;
        this.callFuncIndex=callFuncIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_default);
        setCancelable(false);
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        layoutParams = window.getAttributes();
        dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 3 / 4;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }
    public void goToPlayMarket(){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=hwang.daemin.kangbuk"));
        context.startActivity(intent);
    }
    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvTitle.setText(title);
        tvContent.setText(content);
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (callFuncIndex){
                    case 0: //업데이트시 마켓이동
                        goToPlayMarket();
                        break;
                    default:
                        cancel();
                        break;
                }
                cancel();
            }
        });
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

}