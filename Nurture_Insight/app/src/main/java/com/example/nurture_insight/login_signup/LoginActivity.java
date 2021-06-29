package com.example.nurture_insight.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_profile.TherapistInfo;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText phone, password;
    TextView goToSignUpMessage;
    Button loginButton;
    ProgressDialog loading;
    String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        phone = (EditText) findViewById(R.id.therapistInfo_exitText_expertise);
        password = (EditText) findViewById(R.id.therapistInfo_editText_password);
        loginButton = (Button) findViewById(R.id.therapistInfo_button_saveInfo);
        goToSignUpMessage = (TextView) findViewById(R.id.login_message6);
        loading = new ProgressDialog(this);

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if (userPhoneKey != "" && userPasswordKey != ""){
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey)){
                AllowAccess(userPhoneKey, userPasswordKey);

                loading.setTitle(getString(R.string.pre_login_title));
                loading.setMessage(getString(R.string.pre_login_message));
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

        goToSignUpMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent= new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private void loginUser() {
        String inputPhone = phone.getText().toString();
        String inputPassword = password.getText().toString();

        if (TextUtils.isEmpty(inputPhone)){
            phone.setError(getString(R.string.signUp_error_1));
        }
        else if (TextUtils.isEmpty(inputPassword)){
            password.setError(getString(R.string.signUp_error_3));
        }
        else{
            loading.setTitle(getString(R.string.login_loading_title));
            loading.setMessage(getString(R.string.signUp_loading_message));
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            checkCredentials(inputPhone, inputPassword);
        }
    }

    private void checkCredentials(String inputPhone, String inputPassword) {

        final DatabaseReference RootReference;
        RootReference = FirebaseDatabase.getInstance().getReference();

        RootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(inputPhone).exists()){
                    Users userData = snapshot.child(parentDbName).child(inputPhone).getValue(Users.class);

                    if (userData.getPhoneNo().equals(inputPhone)){
                        if (userData.getPassword().equals(inputPassword)){
                            loading.dismiss();

                            if(userData.getStatus().equals("true")){

                                Paper.book().write(Prevalent.userPhoneKey, inputPhone);
                                Paper.book().write(Prevalent.userPasswordKey, inputPassword);

                                Prevalent.currentOnlineUser = userData;
                                if(Prevalent.currentOnlineUser.getType().equals("therapist")){

                                    DatabaseReference newReference = FirebaseDatabase.getInstance().getReference().child("Therapist");

                                    newReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot therapistSnapshot) {
                                            if(therapistSnapshot.child(inputPhone).exists()){
                                                Intent signUpIntent= new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(signUpIntent);
                                                finish();
                                            }
                                            else{
                                                Intent therapistIntent= new Intent(LoginActivity.this, TherapistInfo.class);
                                                startActivity(therapistIntent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else{
                                    Intent signUpIntent= new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(signUpIntent);
                                    finish();
                                }
                            }
                            else{
                                sendOtpCode();
                            }

                        }
                        else{
                            loading.dismiss();
                            password.setError(getString(R.string.login_error_2));
                        }
                    }
                }
                else{
                    phone.setError(getString(R.string.login_error_1));
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendOtpCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+977" + phone.getText(), 60, TimeUnit.SECONDS,
                LoginActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
               /* progressBar.setVisibility(View.GONE);
                secretCodeSenderBtn.setVisibility(View.VISIBLE);*/

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                /*progressBar.setVisibility(View.GONE);
                secretCodeSenderBtn.setVisibility(View.VISIBLE);
                Toast.makeText(MultiLoginOption.this, e.getMessage(), Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //progressBar.setVisibility(View.GONE);
                        // secretCodeSenderBtn.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(LoginActivity.this, otpActivity.class);
                        String phone_number = phone.getText().toString();
                        intent.putExtra("phone_number", phone_number);
                        intent.putExtra("otp_code", s);
                        startActivity(intent);
                        loading.dismiss();
                    }
                });
    }

    private void AllowAccess(String inputPhone, String inputPassword) {
        final DatabaseReference RootReference;
        RootReference = FirebaseDatabase.getInstance().getReference();

        RootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(inputPhone).exists()){
                    Users userData = snapshot.child(parentDbName).child(inputPhone).getValue(Users.class);

                    if (userData.getPhoneNo().equals(inputPhone)){
                        if (userData.getPassword().equals(inputPassword)){
                            loading.dismiss();
                            Prevalent.currentOnlineUser = userData;

                            if(Prevalent.currentOnlineUser.getType().equals("therapist")){
                                DatabaseReference newReference = FirebaseDatabase.getInstance().getReference().child("Therapist");

                                newReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot therapistSnapshot) {
                                        if(therapistSnapshot.child(inputPhone).exists()){
                                            Intent signUpIntent= new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(signUpIntent);
                                            finish();
                                        }
                                        else{
                                            Intent therapistIntent= new Intent(LoginActivity.this, TherapistInfo.class);
                                            startActivity(therapistIntent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else{
                                Intent signUpIntent= new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(signUpIntent);
                                finish();
                            }

                        }
                        else{
                            loading.dismiss();
                        }
                    }
                }
                else{
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}