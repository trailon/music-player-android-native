package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    EditText usernameEditText, passwordEditText;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final Button loginButton = findViewById(R.id.login_button);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()){
                    if(sharedPref.contains(usernameEditText.getText().toString())){
                        String password = sharedPref.getString(usernameEditText.getText().toString(),"Email Adresi Kayıtlı Değil!");
                        System.out.println(password);
                        if(password.contentEquals(passwordEditText.getText().toString())){
                            Set<String> defValues = new HashSet<>();
                            defValues.add("Default");
                            Set<String> userInfo =  sharedPref.getStringSet(usernameEditText.getText().toString()+"all",defValues);
                            String[] userInfoArray = null;
                            userInfoArray = userInfo.toArray(new String[5]);
                            loadingProgressBar.setVisibility(View.VISIBLE);
                            loginButton.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Hoş Geldiniz! "+ userInfoArray[0], Toast.LENGTH_SHORT).show();
                            System.out.println(usernameEditText.getText().toString());
                            System.out.println(passwordEditText.getText().toString());
                            ToLogin();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            loginButton.setVisibility(View.VISIBLE);
                        }else{
                            count+=1;
                            Toast.makeText(getApplicationContext(), "Şifre Yanlış", Toast.LENGTH_SHORT).show();
                            if(count == 3){
                                count = 0;
                                Toast.makeText(getApplicationContext(), "Kayıt Olmaya!", Toast.LENGTH_SHORT).show();
                                ToRegister();
                            }
                        }
                    }else{
                        count+=1;
                        if(count == 3){
                            count = 0;
                            Toast.makeText(getApplicationContext(), "Kayıt Olmaya!", Toast.LENGTH_SHORT).show();
                            ToRegister();
                        }
                        Toast.makeText(getApplicationContext(), "Email Adresi Kayıtlı Değil!", Toast.LENGTH_SHORT).show();
                    }

                    // LOGİN İŞLEMLERİ
                }else{
                    Toast.makeText(getApplicationContext(), "Kayıt Olmaya!", Toast.LENGTH_SHORT).show();
                    ToRegister();
                    // REGISTERA YÖNLENDİR
                }

            }
        });
    }

    private void ToLogin(){
        Intent intent = new Intent(MainActivity.this,MenuActivity.class);
        MainActivity.this.startActivity(intent);
    }
    private void ToRegister(){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        MainActivity.this.startActivity(intent);
    }


    }
