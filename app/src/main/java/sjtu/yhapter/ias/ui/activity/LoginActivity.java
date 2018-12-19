package sjtu.yhapter.ias.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sjtu.yhapter.common.ui.LoadingDialog;
import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.model.Constants;
import sjtu.yhapter.ias.presenter.LoginPresenter;
import sjtu.yhapter.ias.presenter.contract.LoginContract;
import sjtu.yhapter.ias.ui.base.BaseMVPActivity;
import sjtu.yhapter.ias.ui.base.SimpleTextWatcher;
import sjtu.yhapter.reader.util.SharedPrefUtil;

public class LoginActivity extends BaseMVPActivity<LoginContract.Presenter> implements LoginContract.View {
    private TextView tvRegister;
    private EditText etUsername, etPassword;
    private Button btnLogin;

    private LoadingDialog loadingDialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected LoginContract.Presenter bindPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvRegister = findViewById(R.id.tv_register);
        btnLogin = findViewById(R.id.btn_login);

        loadingDialog = LoadingDialog.getInstance(this);
    }

    @Override
    protected void initListener() {
        super.initListener();

        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.tv_register:
                    startActivity(RegisterActivity.class);
                    break;
                case R.id.btn_login:
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    loadingDialog.show();
                    presenter.login(username, password);
                    break;
            }
        };
        tvRegister.setOnClickListener(ocl);
        btnLogin.setOnClickListener(ocl);

        etUsername.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkAndSetButtonEnable();
            }
        });
        etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkAndSetButtonEnable();
            }
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        String username = SharedPrefUtil.getInstance().getString(Constants.UID);
        if (!TextUtils.isEmpty(username)) {
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onLogin(boolean success, String msg) {
        loadingDialog.dismiss();
        if (success) {
            startActivity(MainActivity.class);
            finish();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
    }

    private void checkAndSetButtonEnable() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        btnLogin.setEnabled(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password));
    }
}
