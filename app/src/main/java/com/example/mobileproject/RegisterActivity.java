package com.example.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends Activity {
    int SELECT_PICTURE = 200;
    ImageView registerImageButton;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    EditText usernameEditText, phoneEditText, emailEditText, passwordEditText, passwordAgainEditText, surnameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        usernameEditText = findViewById(R.id.name_register);
        surnameEditText = findViewById(R.id.surname_register);
        phoneEditText = findViewById(R.id.phone_register);
        emailEditText = findViewById(R.id.email_register);
        passwordEditText = findViewById(R.id.password_register);
        passwordAgainEditText = findViewById(R.id.password_again);
        Button register = findViewById(R.id.register);
        registerImageButton = findViewById(R.id.image_picker);
        usernameEditText.addTextChangedListener(new TextValidator(usernameEditText) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void validate(TextView textView, String text) {
                if(!usernameEditText.getText().toString().matches("^[A-Za-z ]+$")){
                    usernameEditText.setError(Html.fromHtml("<p>İsim özel karakter içeremez</p><br><br>", Html.FROM_HTML_MODE_COMPACT));
                }
            }
        });
        surnameEditText.addTextChangedListener(new TextValidator(surnameEditText) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void validate(TextView textView, String text) {
                if(!surnameEditText.getText().toString().matches("^[A-Za-z]+$")){
                    surnameEditText.setError(Html.fromHtml("<p>Soyisim özel karakter içeremez</p><br><br>", Html.FROM_HTML_MODE_COMPACT));
                }
            }
        });
        phoneEditText.addTextChangedListener(new TextValidator(phoneEditText) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void validate(TextView textView, String text) {
                if(!phoneEditText.getText().toString().matches("^[0-9]{10,13}$")){
                    phoneEditText.setError(Html.fromHtml("<p>Uygun bir numara giriniz</p><br><br>", Html.FROM_HTML_MODE_COMPACT));
                }
            }
        });
        emailEditText.addTextChangedListener(new TextValidator(emailEditText) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void validate(TextView textView, String text) {
                if(!emailEditText.getText().toString().matches("^(.+)@(.+)$")){
                    emailEditText.setError(Html.fromHtml("<p>Uygun bir email adresi giriniz</p><br><br>", Html.FROM_HTML_MODE_COMPACT));
                }
            }
        });
        passwordEditText.addTextChangedListener(new TextValidator(passwordEditText) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void validate(TextView textView, String text) {
                if(passwordEditText.getText().toString().length() < 6){
                    passwordEditText.setError(Html.fromHtml("<p>Şifreniz en az 6 karakterli olmalı", Html.FROM_HTML_MODE_COMPACT));
                }
            }
        });
        passwordAgainEditText.addTextChangedListener(new TextValidator(passwordAgainEditText) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void validate(TextView textView, String text) {
                if(!passwordAgainEditText.getText().toString().equals(passwordEditText.getText().toString())){
                    passwordAgainEditText.setError(Html.fromHtml("<p>Şifreler aynı olmalı</p><br><br>", Html.FROM_HTML_MODE_COMPACT));
                }
            }
        });
        registerImageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Boolean result = validation();
                if(result){
                    if(sharedPref.contains(emailEditText.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Email adresi zaten kayıtlı!", Toast.LENGTH_SHORT).show();
                    }else{
                        Set<String> userdata = new HashSet<>();
                        userdata.add(usernameEditText.getText().toString());
                        userdata.add(surnameEditText.getText().toString());
                        userdata.add(phoneEditText.getText().toString());
                        userdata.add(emailEditText.getText().toString());
                        userdata.add(passwordEditText.getText().toString());
                        editor.putStringSet(emailEditText.getText().toString()+"all", userdata);
                        System.out.println(passwordEditText.getText().toString());
                        editor.putString(emailEditText.getText().toString(),passwordEditText.getText().toString());
                        editor.apply();
                        Toast.makeText(getApplicationContext(),"Kayıt Oldunuz!", Toast.LENGTH_SHORT).show();
                        SendEmail();
                        finish();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Lütfen alanları doğru girdiğinizden emin olunuz!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendEmail() {
            try {
                String stringSenderEmail = "mymusicplayerserdem@gmail.com";
                String stringReceiverEmail = emailEditText.getText().toString();
                String stringPasswordSenderEmail = "serdem963214785";

                String stringHost = "smtp.gmail.com";

                Properties properties = System.getProperties();

                properties.put("mail.smtp.host", stringHost);
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                mimeMessage.setSubject("My Music Player Kayıt İşlemi");
                mimeMessage.setText("Merhabalar My Music Player'a başarılı bir şekilde kayıt olmuş bulunmaktasınız!\nSerdem İrdem");

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Transport.send(mimeMessage);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean validation() {
        if(!usernameEditText.getText().toString().matches("^[A-Za-z ]+$")){
            return false;
        }
        if(!surnameEditText.getText().toString().matches("^[A-Za-z]+$")){
            return false;
        }
        if(!phoneEditText.getText().toString().matches("^[0-9]{10,13}$")){
            return false;
        }
        if(!emailEditText.getText().toString().matches("^(.+)@(.+)$")){
            return false;
        }
        if(passwordEditText.getText().toString().length() < 6){
            return false;
        }
        if(!passwordAgainEditText.getText().toString().equals(passwordEditText.getText().toString())){
            return false;
        }
        return true;
    }
    public abstract class TextValidator implements TextWatcher {
        private final TextView textView;

        public TextValidator(TextView textView) {
            this.textView = textView;
        }

        public abstract void validate(TextView textView, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = textView.getText().toString();
            validate(textView, text);
        }

        @Override
        final public void
        beforeTextChanged(CharSequence s, int start, int count, int after) {
            /* Needs to be implemented, but we are not using it. */
        }

        @Override
        final public void
        onTextChanged(CharSequence s, int start, int before, int count) {
            /* Needs to be implemented, but we are not using it. */
        }
    }

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    registerImageButton.setImageURI(selectedImageUri);
                }
            }
        }
    }
}