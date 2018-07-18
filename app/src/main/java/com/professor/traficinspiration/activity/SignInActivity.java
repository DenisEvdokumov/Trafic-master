package com.professor.traficinspiration.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.professor.traficinspiration.ApplicationContext;
import com.professor.traficinspiration.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText passwordEditText;
    TextView emailEditText;
    CheckBox remainSignedInCheckBox;


    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.back_button).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.header_title)).setText("Авторизация");

//        ApplicationContext.setContext(this);

        email = ApplicationContext.getUser().getEmail();

        emailEditText = (TextView) findViewById(R.id.et_email);
        emailEditText.setText(email);

        passwordEditText = (EditText) findViewById(R.id.et_password);
//        remainSignedInCheckBox = (CheckBox) findViewById(R.id.cb_remain_signed_in);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
//        findViewById(R.id.forgot_password).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String password;
        String email;
        switch (v.getId()) {
            case R.id.btn_login:
                password = passwordEditText.getText().toString();
                email = emailEditText.getText().toString();

                // password check
                if (password.equals("")|email.equals("")){
                    Toast.makeText(SignInActivity.this, "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
                    return;
                }

                this.finish();

                ApplicationContext.getMessageService().executeEnterSequence(email, password, "authorization", 0L);

                // continued in MessageService onResponse
                break;

            case R.id.btn_register:

                Intent toRegistrationActivity = new Intent(this, RegistrationActivity.class);
                this.startActivity(toRegistrationActivity);

//                this.finish();

                break;
//            case R.id.forgot_password:
//
//                Toast.makeText(SignInActivity.this, "Сочувствую :-). Функция востановления пароля пока не поддерживается.", Toast.LENGTH_SHORT).show();
//
//                break;

            case R.id.back_button:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
