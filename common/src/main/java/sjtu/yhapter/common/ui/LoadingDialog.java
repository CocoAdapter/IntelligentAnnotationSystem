package sjtu.yhapter.common.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;

import sjtu.yhapter.common.R;

public class LoadingDialog extends AlertDialog {
    private static volatile LoadingDialog loadingDialog;

    private AVLoadingIndicatorView avi;

    public static LoadingDialog getInstance(Context context) {
        if (loadingDialog == null) {
            synchronized (LoadingDialog.class) {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(context, R.style.TransparentDialog); //设置AlertDialog背景透明
                    loadingDialog.setCancelable(false);
                    loadingDialog.setCanceledOnTouchOutside(false);
                }
            }
        }
        return loadingDialog;
    }

    private LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        avi = this.findViewById(R.id.avi);
    }

    @Override
    public void show() {
        super.show();
        avi.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }
}
