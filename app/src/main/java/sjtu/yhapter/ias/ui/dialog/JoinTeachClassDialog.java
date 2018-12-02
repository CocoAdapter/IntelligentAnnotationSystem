package sjtu.yhapter.ias.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.ui.activity.MainActivity;

public class JoinTeachClassDialog extends Dialog {
    private static final int CODE_LENGTH = 11;


    private EditText etClassNum;
    private Button btnJoin;

    private Action action;

    public JoinTeachClassDialog(@NonNull Context context, Activity ownerActivity) {
        super(context, R.style.CommonDialog);
        setOwnerActivity(ownerActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_join_teach_class);

        initData();
        initWidget();
        initListener();
    }

    private void initData() {

    }

    private void initWidget() {
        etClassNum = findViewById(R.id.et_class_num);
        btnJoin = findViewById(R.id.btn_join);

        Window window = getWindow();
        Activity activity = getOwnerActivity();
        if (window != null && activity != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            Point p = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(p);
            lp.width = (int) (p.x * 0.8);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
    }

    private void initListener() {
        etClassNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnJoin.setEnabled(s.length() == CODE_LENGTH);
            }
        });
        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.btn_join:
                    dismiss();
                    if (action != null)
                        action.onJoinClick(Long.parseLong(etClassNum.getText().toString()));
                    break;
            }
        };
        btnJoin.setOnClickListener(ocl);
    }

    public void setOnJoinClickListener(Action action) {
        this.action = action;
    }

    public interface Action {
        void onJoinClick(long code);
    }
}
