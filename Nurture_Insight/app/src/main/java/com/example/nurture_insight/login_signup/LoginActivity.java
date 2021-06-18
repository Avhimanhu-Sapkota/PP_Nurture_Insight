package com.example.nurture_insight.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.SplashActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        phone = (EditText) findViewById(R.id.login_exitText_phoneNo);
        password = (EditText) findViewById(R.id.login_editText_password);
        loginButton = (Button) findViewById(R.id.login_button);
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

        Paper.book().write(Prevalent.userPhoneKey, inputPhone);
        Paper.book().write(Prevalent.userPasswordKey, inputPassword);

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
                            Intent signUpIntent= new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(signUpIntent);
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
                            Intent signUpIntent= new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(signUpIntent);
                        }
                        else{
                            loading.dismiss();
                            //password.setError(getString(R.string.login_error_2));
                        }
                    }
                }
                else{
                    //phone.setError(getString(R.string.login_error_1));
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}