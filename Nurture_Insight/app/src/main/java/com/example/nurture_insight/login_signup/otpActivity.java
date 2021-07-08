package com.example.nurture_insight.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.assessment.Self_care_assessment;
import com.example.nurture_insight.therapist_dashboard.therapistDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class otpActivity extends AppCompatActivity {

    Button otpSubmit;
    PinView otpAnswer;
    String otpcode, phoneNo;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpAnswer = (PinView) findViewById(R.id.otpcode);
        otpSubmit = (Button) findViewById(R.id.optSubmitButton);
        loading = new ProgressDialog(this);

        otpcode = getIntent().getStringExtra("otp_code");
        phoneNo = getIntent().getStringExtra("phone_number");
        Log.d("otp_code", "onCreate: "+otpcode);

        otpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setTitle("Nurture Insight - Verification");
                loading.setMessage("Please Wait! We are checking your OTP");
                loading.setCanceledOnTouchOutside(false);
                loading.show();

                String inputOtp = otpAnswer.getText().toString();
                Log.d("otp_code", "onCreate: "+inputOtp);
                if(otpcode != null){
                        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(otpcode,inputOtp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    final DatabaseReference RootReference;
                                    RootReference = FirebaseDatabase.getInstance().getReference().child("Users").child(phoneNo);

                                    HashMap<String, Object> newHash = new HashMap<>();
                                    newHash.put("status", "true");

                                    RootReference.updateChildren(newHash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                String from = getIntent().getStringExtra("from");
                                                Log.d("UNIQUENAME", "onComplete: " + from);
                                                if (from!=null){
                                                    HashMap<String, Object> userMap = new HashMap<>();
                                                    userMap.put("therapistID", Prevalent.currentOnlineUser.getPhoneNo());

                                                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                                            .child("Users").child(phoneNo);

                                                    userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Intent intent = new Intent(otpActivity.this, MainActivity.class);
                                                                intent.putExtra("status", "otpDone");
                                                                startActivity(intent);
                                                                finish();

                                                                loading.dismiss();
                                                            }
                                                            else{

                                                                loading.dismiss();
                                                                Intent intent = new Intent(otpActivity.this, MainActivity.class);
                                                                intent.putExtra("status", "otpDone");
                                                                startActivity(intent);
                                                                finish();

                                                            }
                                                        }
                                                    });

                                                }
                                                else{
                                                    Intent intent = new Intent(otpActivity.this, LoginActivity.class);
                                                    intent.putExtra("otpStatus", "true");
                                                    startActivity(intent);
                                                    loading.dismiss();
                                                }

                                            }
                                        }
                                    });


                                }
                                else{
                                    Toast.makeText(otpActivity.this, "Enter the correct otp code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }

            }
        });

    }


}
