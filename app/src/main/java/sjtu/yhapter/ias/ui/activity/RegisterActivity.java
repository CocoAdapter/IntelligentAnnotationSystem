package sjtu.yhapter.ias.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import sjtu.yhapter.common.ui.LoadingDialog;
import sjtu.yhapter.ias.R;
import sjtu.yhapter.ias.presenter.RegisterPresenter;
import sjtu.yhapter.ias.presenter.contract.RegisterContract;
import sjtu.yhapter.ias.ui.base.BaseMVPActivity;
import sjtu.yhapter.ias.ui.base.SimpleTextWatcher;

public class RegisterActivity extends BaseMVPActivity<RegisterContract.Presenter> implements RegisterContract.View {
    private ImageView imgBack;
    private EditText etName, etStuId, etPhone, etPassword, etPasswordConfirm;
    private Button btnRegister;

    private LoadingDialog loadingDialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_register;
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected RegisterContract.Presenter bindPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        imgBack = findViewById(R.id.img_back);
        etName = findViewById(R.id.et_name);
        etStuId = findViewById(R.id.et_stuid);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etPasswordConfirm = findViewById(R.id.et_password_confirm);
        btnRegister = findViewById(R.id.btn_register);

        loadingDialog = LoadingDialog.getInstance(this);
    }

    @Override
    protected void initListener() {
        super.initListener();

        View.OnClickListener ocl = v -> {
            switch (v.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.btn_register:
                    String name = etName.getText().toString();
                    String stuId = etStuId.getText().toString();
                    String phone = etPhone.getText().toString();
                    String password = etPassword.getText().toString();
                    loadingDialog.show();
                    presenter.register(name, stuId, phone, password);
                    break;
            }
        };
        imgBack.setOnClickListener(ocl);
        btnRegister.setOnClickListener(ocl);

        etName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkAndSetButtonEnable();
            }
        });
        etStuId.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkAndSetButtonEnable();
            }
        });
        etPhone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                checkAndSetButtonEnable();
            }
        });
        etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (!TextUtils.isEmpty(password)) {
                    if (password.length() < getResources().getInteger(R.integer.password_min_Length)) {
                        etPasswordConfirm.setError(getString(R.string.password_length_illegal));
                    } else {
                        etPasswordConfirm.setError(null);

                        String passwordConfirm = etPasswordConfirm.getText().toString();
                        if (password.equals(passwordConfirm)) {
                            etPasswordConfirm.setError(null);
                        } else
                            etPasswordConfirm.setError(getString(R.string.password_not_match));
                    }

                }
                checkAndSetButtonEnable();
            }
        });
        etPasswordConfirm.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (!TextUtils.isEmpty(password)) {
                    if (password.length() < getResources().getInteger(R.integer.password_min_Length)) {
                        etPasswordConfirm.setError(getString(R.string.password_length_illegal));
                    } else {
                        etPasswordConfirm.setError(null);

                        String passwordConfirm = etPassword.getText().toString();
                        if (password.equals(passwordConfirm)) {
                            etPasswordConfirm.setError(null);
                        } else
                            etPasswordConfirm.setError(getString(R.string.password_not_match));
                    }
                }
                checkAndSetButtonEnable();
            }
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();
    }

    @Override
    public void onRegister(boolean success, String msg) {
        loadingDialog.hide();
        if (success) {
            startActivity(LoginActivity.class);
            finish();
        } else
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void checkAndSetButtonEnable() {
        boolean name = !TextUtils.isEmpty(etName.getText().toString());
        boolean stuId = !TextUtils.isEmpty(etStuId.getText().toString());
        boolean phone = !TextUtils.isEmpty(etPhone.getText().toString());
        boolean password = !TextUtils.isEmpty(etPassword.getText().toString());
        boolean passwordConfirm = !TextUtils.isEmpty(etPasswordConfirm.getText().toString());

        boolean passwordMatch = etPasswordConfirm.getError() == null;
        btnRegister.setEnabled(name && stuId && phone && password && passwordConfirm && passwordMatch);
    }
}
