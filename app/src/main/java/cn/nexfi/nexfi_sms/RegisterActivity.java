package cn.nexfi.nexfi_sms;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import cn.smssdk.SMSSDK;

/**
 * Created by Mark on 2016/6/15.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Runnable {
    private Button btn_register;
    private ImageView iv_back;
    private TextView tv_login;
    private EditText et_phone;

    private Handler handler;
    private AlertDialog mAlertDialog;
    private String phoneNunmber;

    private static String APPKEY = "13d214a6322ee";
    private static String APPSECRET = "1bc425c9f5ddd401b7f61c35dd216711";
    private static final String REGEX_MOBILE = "^((17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$";

    private String intentName = "phoneNunmber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
    }

    private void initHandler() {
        handler = new Handler();
        handler.postDelayed(this, 500);
    }


    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog, null);
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(v);
    }


    private void initView() {
        btn_register = (Button) findViewById(R.id.btn_register);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_login = (TextView) findViewById(R.id.tv_login);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_register.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }


    private boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_register:
                if (!TextUtils.isEmpty(et_phone.getText())) {
                    phoneNunmber = et_phone.getText().toString();
                    if (isMobile(phoneNunmber)) {
                        initDialog();
                        initHandler();
                        SMSSDK.getVoiceVerifyCode("86", phoneNunmber);
                        Toast.makeText(getApplicationContext(), "发送验证码成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                        intent.putExtra(intentName, phoneNunmber);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_login:
                startActivity(new Intent(RegisterActivity.this, LonginActivity.class));
                finish();
                break;
        }
    }


    @Override
    public void run() {
        mAlertDialog.dismiss();
    }
}
