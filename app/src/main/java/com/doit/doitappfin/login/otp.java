package com.doit.doitappfin.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.doit.doitappfin.R;
import com.doit.doitappfin.ui.MainWorkActivity;
import com.doit.doitappfin.utils.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    TextView resendotp;
    EditText reenterphone,enterotp;
    Button verifyotp;
    String number;
    FirebaseAuth auth;
    String verificationId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        number = getIntent().getExtras().getString("number");

        reenterphone = findViewById(R.id.reenterphone);
        resendotp = findViewById(R.id.resendotp);
        enterotp=findViewById(R.id.enterotp);
        auth=FirebaseAuth.getInstance();

        verifyotp = findViewById(R.id.sendotp);
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode(enterotp.getText().toString());
            }
        });


        reenterphone.setText(number);

        sendVerificationCode(UserDetails.sphone);
        resendotp.setVisibility(View.VISIBLE);

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendotp.setVisibility(View.GONE);
                sendVerificationCode(reenterphone.getText().toString());
                resendotp.setVisibility(View.VISIBLE);

            }
            //StartFirebaseLogin();


        });


    }


    private void sendVerificationCode(String number) {

//        String phoneNumber = number;
//        // Check phoneNumber.length()
//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//        try {
//            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "IN");
//            Toast.makeText(this, phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164), Toast.LENGTH_SHORT).show();
//            PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                    phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164), // Phone number to verify
//                    6, // Timeout duration
//                    TimeUnit.SECONDS, // Unit of timeout
//                    otp.this, // Activity (for callback binding)
//                    mCallbacks); // OnVerificationStateChangedCallbacks
//        } catch (NumberParseException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            // Wrong format
//        }





        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            SignInWithCredential(phoneAuthCredential);
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                enterotp.setText(code);
                //String codenum=enterotp.getText().toString();

                verifyCode(code);


            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    private void SignInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRef = database.getReference().child("ProfileData").child(UserDetails.semail.replace(".","_"));

                            HashMap<String,String> hashMap=new HashMap<>();


                            hashMap.put("name", UserDetails.sname);
                            hashMap.put("email",UserDetails.semail);
                            hashMap.put("number",UserDetails.sphone);
                            hashMap.put("date","NO");
                            hashMap.put("city","NO");
                            hashMap.put("addr","NO");
                            hashMap.put("sex","NO");
                            hashMap.put("image","https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png");
                            hashMap.put("iscomplete","NO");
                            mRef.setValue(hashMap);
                            FirebaseDatabase.getInstance().getReference().child("LoginData").child(UserDetails.semail.replace(".","_")).setValue(UserDetails.sphone);


                            startActivity(new Intent(otp.this, MainWorkActivity.class));
                            finish();
                        } else {


                        }
                    }
                });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        SignInWithCredential(credential);
    }
    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }

        return false;

    }


    public void NoInternetAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are not connected to the internet. ");
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(connectedToNetwork()){
                    //volley();
                }else{ NoInternetAlertDialog(); }
            }
        });
        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openSettings = new Intent();
                openSettings.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                openSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openSettings);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}