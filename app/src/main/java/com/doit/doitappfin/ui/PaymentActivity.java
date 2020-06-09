package com.doit.doitappfin.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.ProfileData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {

private TextView Tname,Tloc,Tprice,Tdis,Ttot;
private  Button btproceed;
private ImageView imageView;
private String  Sname="",Sloc="",Sprice="0",Stot="",Simg="",Sdis="",Sid="",Sdispercent="";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    String mail="",num="",oiscomp="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent i=getIntent();

        Sname=i.getStringExtra("name");
        Sprice=i.getStringExtra("price");
        Sdis=i.getStringExtra("discount");
        Sloc=i.getStringExtra("location");
        Simg=i.getStringExtra("image");
        Sid=i.getStringExtra("id");

        Stot=Integer.parseInt(Sprice.replace(",",""))-Integer.parseInt(Sdis.replace(",",""))+"";
        Sdispercent=((Integer.parseInt(Sdis.replace(",",""))*100)/Integer.parseInt(Sprice.replace(",","")))+"";




      //  Toast.makeText(this, Sid, Toast.LENGTH_SHORT).show();  dis*100/total=recent
         btproceed=findViewById(R.id.proceedpay);


        imageView=findViewById(R.id.imagev);

        Tname=findViewById(R.id.TVTrainTitle);
        Tloc=findViewById(R.id.TVTrainLocation);
        Tprice=findViewById(R.id.TVTrainFee);
        Tdis=findViewById(R.id.TVTraindis);
        Ttot=findViewById(R.id.TVTrainTotal);


        if(mAuth.getCurrentUser() == null){
            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        }
        acct = GoogleSignIn.getLastSignedInAccount(this);
        getData();


    setData();

    btproceed.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(Sprice!=null && iscopmlete())
            startPayment(Sprice);
        }
    });
    }

    private boolean iscopmlete() {
        if(oiscomp.equals("NO"))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please complete your profile and proceed ");
            builder.setPositiveButton("my profile", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                    startActivity(new Intent(PaymentActivity.this,proflie.class));
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
        else return oiscomp.equals("YES");

    }
    private void getData() {
        if(acct!= null) {
            System.out.println("hr");
            mail=acct.getEmail().replace(".","_");
            FirebaseDatabase.getInstance().getReference().child("ProfileData").child(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ProfileData obj=dataSnapshot.getValue(ProfileData.class);

                    oiscomp=(obj.getIscomplete());

                    System.out.println("in single acc"+oiscomp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            System.out.println("mr");

            SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);

            num=sp.getString("number","");
            // System.out.println(omail+" "+onum);

            if(num.length()==10)
            {


                FirebaseDatabase.getInstance().getReference().child("LoginData").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d1:dataSnapshot.getChildren())
                        {
                            if((d1.getValue().toString()).equals(num))
                            {
                                // System.out.println(d1.getValue().toString()+" "+onum);
                                mail= d1.getKey().trim();

                                FirebaseDatabase.getInstance().getReference().child("ProfileData").child(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        ProfileData obj=dataSnapshot.getValue(ProfileData.class);

                                        oiscomp=(obj.getIscomplete());

                                        System.out.println("in single ph"+oiscomp);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        }


    }

    private void setData() {

        Tprice.setText(Sprice+" Rs");
        Tname.setText(Sname);
        Tloc.setText(Sloc);
        Tdis.setText(Sdispercent+"%");
        Ttot.setText(Stot+" Rs");

        if(Simg!=null)
        Glide.with(this)
                .load(Simg).fitCenter().override(1000,1000)
                .into(imageView);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        String paymentId = paymentData.getPaymentId();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("ammount",""+Sprice);
        hashMap.put("image",Simg);
        hashMap.put("location",Sloc);
        hashMap.put("other","");
        hashMap.put("paymentid",paymentId);
        hashMap.put("time",""+formatter.format(date));
        hashMap.put("title",Sname);

        String id=""+java.time.LocalDate.now()+"_"+Sid;

        FirebaseDatabase.getInstance().getReference().child("Payment").child(mail).child("Training").child(id).setValue(hashMap);
        Intent i= new Intent(this,MyCourseActivity.class);

        startActivity(i);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }

    private void startPayment( String amount) {
        Double amt=Double.parseDouble(amount);
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logodoit);
        final Activity activity = this;
        try{ JSONObject options = new JSONObject();
            options.put("description","#DOIT_PAYMENT");
            options.put("Currency","INR");
            options.put("amount",amt*100);
            options.put("payment_capture", true);
            options.put("email",mail);
            options.put("contact",num);
            checkout.open(activity,options);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
