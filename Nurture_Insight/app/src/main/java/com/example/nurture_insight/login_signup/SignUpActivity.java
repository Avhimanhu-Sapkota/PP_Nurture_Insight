package com.example.nurture_insight.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText phoneNo, email, password, username;
    TextView goToLoginMessage;
    Button signUpButton;
    ProgressDialog loading, newLoading;
    String otpStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.signUp_exitText_username);
        phoneNo = (EditText) findViewById(R.id.signUp_exitText_phoneNo);
        email = (EditText) findViewById(R.id.signUp_exitText_email);
        password = (EditText) findViewById(R.id.signUp_editText_password);
        goToLoginMessage = (TextView) findViewById(R.id.signUp_message5);
        signUpButton = (Button) findViewById(R.id.signUp_button);
        loading = new ProgressDialog(this);
        newLoading = new ProgressDialog(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });

        goToLoginMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent= new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void RegisterUser() {
        String inputPhoneNo = phoneNo.getText().toString();
        String inputEmail = email.getText().toString();
        String inputPassword = password.getText().toString();
        String inputName = username.getText().toString();

        if (TextUtils.isEmpty(inputPhoneNo)){
            phoneNo.setError(getString(R.string.signUp_error_1));
        }
        else if(inputPhoneNo.length()!=10){
            phoneNo.setError(getString(R.string.signUp_error_8));
        }
        else if (TextUtils.isEmpty(inputEmail)){
            email.setError(getString(R.string.signUp_error_2));
        }
        else if(!isValidEmail(inputEmail)){
            email.setError(getString(R.string.signUp_error_7));
        }
        else if (TextUtils.isEmpty(inputPassword)){
            password.setError(getString(R.string.signUp_error_3));
        }
        else if(inputPassword.length()<8){
            password.setError(getString(R.string.signUp_error_9));
        }
        else if(!isValidPassword(inputPassword)){
            password.setError(getString(R.string.signUp_error_6));
        }
        else if (TextUtils.isEmpty(inputName)){
            username.setError(getString(R.string.signUp_error_11));
        }
        else{
            loading.setTitle(getString(R.string.signUp_loading_title));
            loading.setMessage(getString(R.string.signUp_loading_message));
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            ValidateUser(inputPhoneNo, inputName, inputEmail, inputPassword);
        }

    }

    private void ValidateUser(String inputPhoneNo, String inputName, String inputEmail, String inputPassword) {

        final DatabaseReference RootReference;
        RootReference = FirebaseDatabase.getInstance().getReference();

        RootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(inputPhoneNo).exists())){
                    String status = "false";
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phoneNo", inputPhoneNo);
                    userDataMap.put("username", inputName);
                    userDataMap.put("email", inputEmail);
                    userDataMap.put("password", inputPassword);
                    userDataMap.put("type", "user");
                    userDataMap.put("therapistID", "0000000000");
                    userDataMap.put("status", status);

                    RootReference.child("Users").child(inputPhoneNo).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, getString(R.string.signUp_success_msg), Toast.LENGTH_SHORT).show();
                                        loading.dismiss();

                                        Intent signUpIntent= new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(signUpIntent);
                                        finish();
                                    }
                                    else{
                                        loading.dismiss();

                                        new AlertDialog.Builder(SignUpActivity.this)
                                                .setTitle(getString(R.string.signUp_loading_title))
                                                .setMessage(R.string.signUp_error_5)
                                                .setCancelable(false)
                                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                            }
                                                        }).show();
                                    }
                                }
                            });
                }
                else{
                    phoneNo.setError(getString(R.string.signUp_error_4));
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static boolean isValidPassword(final String inputPassword){

        String password_pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(inputPassword);

        return  matcher.matches();
    }

    public static boolean isValidEmail(String inputEmail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches();
    }
}