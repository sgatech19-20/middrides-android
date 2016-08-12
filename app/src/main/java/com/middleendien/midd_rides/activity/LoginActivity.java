package com.middleendien.midd_rides.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.middleendien.midd_rides.R;
import com.middleendien.midd_rides.utils.HardwareUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A login screen that offers login via email/password.
 *
 * Note: This class is the entry point of the app
 *
 */
public class LoginActivity extends AppCompatActivity {

    // UI
    private Button btnLogIn;
    private Button btnRegister;
    private AutoCompleteTextView usernameBox;
    private EditText passwdBox;

    private static final int REGISTER_REQUEST_CODE = 0x001;

    private static final int REGISTER_SUCCESS_RESULT_CODE = 0x101;

    private static final int LOGIN_CANCEL_RESULT_CODE = 0x301;

    private static final int PERMISSION_INTERNET_REQUEST_CODE = 0x201;

    private SweetAlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // TODO:
//        if(ParseUser.getCurrentUser() != null){
//            Log.d("LoginActivity", "Already has user");
//            Intent toMainScreen = new Intent(LoginActivity.this, MainActivity.class);
//            toMainScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            toMainScreen.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            NavUtils.navigateUpTo(this, toMainScreen);
//        }

        initData();

        initView();

        initEvent();
    }

    private void initData() {
        // TODO:
//        loginAgent = LoginAgent.getInstance(this);
//        loginAgent.registerListener(LoginAgent.LOGIN, this);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(getString(R.string.waiting_to_log_out), false).apply();
    }

    private void initView() {
        btnLogIn = (Button) findViewById(R.id.login_login);
        btnRegister = (Button) findViewById(R.id.login_register);

        usernameBox = (AutoCompleteTextView) findViewById(R.id.login_email);
        passwdBox = (EditText) findViewById(R.id.login_passwd);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(null);
            getWindow().setReenterTransition(null);
        }

        // adjust logo
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ImageView splashLogo = (ImageView) findViewById(R.id.login_app_logo);

        splashLogo.getLayoutParams().width = (int)(metrics.widthPixels * 0.5);
        splashLogo.getLayoutParams().height = (int)(metrics.heightPixels * 0.4);
    }

    private void initEvent() {
        usernameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0 && s.charAt(s.length() - 1) == '@') {             // ends with "@"
                    ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(
                            LoginActivity.this,
                            android.R.layout.simple_dropdown_item_1line, new String[] { s + "middlebury.edu" });
                    usernameBox.setAdapter(autoCompleteAdapter);
                } else if (s.toString().length() > 2 && !s.toString().contains("@")) {         // "sth"
                    ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(
                            LoginActivity.this,
                            android.R.layout.simple_dropdown_item_1line, new String[] { s + "@middlebury.edu" });
                    usernameBox.setAdapter(autoCompleteAdapter);
                } else if (s.toString().length() > 15 && s.toString().substring(s.length() - 15).equals("@middlebury.edu")) {
                    // completed format
                    usernameBox.dismissDropDown();
                } else if (s.toString().length() == 0) {
                    // cleared everything or initial state, without @
                    usernameBox.setAdapter(null);
                }               // else do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith("@middlebury.edu")) {
                    passwdBox.clearFocus();
                    passwdBox.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(passwdBox, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // login logic is implemented with the LoginAgent class
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED) {
                    // check e-mail validity
                    // TODO:
//                    if (!LoginAgent.isEmailValid(usernameBox.getText().toString())) {
//                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.wrong_email), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    if (!HardwareUtil.isNetworkAvailable(getApplicationContext())){
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_internet_warning), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setDialogShowing(true);
                    // TODO:
//                    loginAgent.loginInBackground(usernameBox.getText().toString(), passwdBox.getText().toString());
                } else {        // no internet permission
                    requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET_REQUEST_CODE);
                }

                hideKeyboard();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                        // switch to register page
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent toRegisterScreen = new Intent(LoginActivity.this, RegisterActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivityForResult(toRegisterScreen, REGISTER_REQUEST_CODE,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    } else {
                        startActivityForResult(toRegisterScreen, REGISTER_REQUEST_CODE);
                    }
                } else {        // no internet permission
                    requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET_REQUEST_CODE);
                }
            }
        });
    }

    private void setDialogShowing(boolean showing) {
        if (showing) {
            progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(getString(R.string.dialog_logging_in));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.showCancelButton(false);
            progressDialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.colorAccent));
            progressDialog.show();
        } else {
            if (progressDialog.isShowing())
                progressDialog.dismissWithAnimation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REGISTER_REQUEST_CODE:
                if(resultCode == REGISTER_SUCCESS_RESULT_CODE){
                    Intent toMainScreen = new Intent(LoginActivity.this, MainActivity.class);
                    toMainScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    toMainScreen.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    NavUtils.navigateUpTo(this, toMainScreen);
                } else {
                    // Register not successful, do nothing
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            // so you have a keyboard, so what?
        }
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_INTERNET_REQUEST_CODE:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)   // not granted
                    Toast.makeText(LoginActivity.this, getString(R.string.permission_internet_denied), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Automatically hide keyboard if touches background
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setResult(LOGIN_CANCEL_RESULT_CODE);
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
