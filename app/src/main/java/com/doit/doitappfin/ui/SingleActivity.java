package com.doit.doitappfin.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SingleActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    public TextView theading,twithou,twith,tinr,ttax,tdesc,ac,discount,rp,op;
    public ImageView imgfin,arr,arrless;
    public Button btproceed,addc;
    private LinearLayout LinGst;
    public EditText coupon;
    public LinearLayout apply,dis,real,red;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    Double DollarPrice=0.0;
    String mail="",num="",oiscomp="",sid="";

    String shead="",swith="",swithot="",stax="",sinr="0",sdesc="",simage="";

    @Override
    protected void onStart() {


        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);


        dis=findViewById(R.id.dis);
        real=findViewById(R.id.real);
        red=findViewById(R.id.red);
        discount=findViewById(R.id.discount);
        rp=findViewById(R.id.rp);
        op=findViewById(R.id.op);
        ac=findViewById(R.id.ac);
        arr=findViewById(R.id.arr);
        arrless=findViewById(R.id.arrless);
        apply=findViewById(R.id.apply);
        addc=findViewById(R.id.addc);
        coupon=findViewById(R.id.coupon);
        theading=findViewById(R.id.textView9);
        tdesc=findViewById(R.id.textView10);
        twithou=findViewById(R.id.withoutTa);
        twith=findViewById(R.id.withTa);
        tinr=findViewById(R.id.inr);
        ttax=findViewById(R.id.tax);
        imgfin=findViewById(R.id.imageView3);
        btproceed=findViewById(R.id.button3);
        LinGst=findViewById(R.id.linearLayout3);



        if(mAuth.getCurrentUser() == null){
            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        }
        acct = GoogleSignIn.getLastSignedInAccount(this);

        FirebaseDatabase.getInstance().getReference().child("dollar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DollarPrice=Double.parseDouble(dataSnapshot.getValue().toString());
                System.out.println("Dollar "+DollarPrice);
                getData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        btproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swith!=null && iscopmlete()) {
                    String rp=tinr.getText().toString();
                     rp=rp.substring(0,rp.indexOf(" "));
                    startPayment(rp);
                }
            }
        });
        System.out.println("vv "+sid);
       // Toast.makeText(this, sid, Toast.LENGTH_SHORT).show();


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

        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }

    }

    private boolean iscopmlete() {

        if(oiscomp.equals("NO"))
{
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Please complete your profile and proceed ");
    builder.setPositiveButton("my profile", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            startActivity(new Intent(SingleActivity.this,proflie.class));
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

    private void volley() {


        Intent i=getIntent();
        shead=i.getStringExtra("title");
        sdesc=i.getStringExtra("desc");
        simage=i.getStringExtra("image");
        sid=i.getStringExtra("id");

        String[] tot = i.getStringExtra("price").split("-");


        if(tot.length==2) {
            swithot = tot[0];
            swith = tot[1];
            twithou.setText(swithot);
            twith.setText(swith);

            if(swith.equals(swithot))
            {
                LinGst.setVisibility(View.GONE);
            }



            if(swith.contains("$")){
                String s=swith.substring(0,swith.indexOf("$"));

                if(s.contains(".")){

                    s=s.substring(0,s.indexOf("."));

                    System.out.println(DollarPrice);
                    System.out.println(s);

                    double p = Double.parseDouble(s) * DollarPrice;
                        sinr = p + "";
                        tinr.setText(p + " Rs");



                }else{
                    System.out.println(DollarPrice);
                    System.out.println(s);
                    double p = Double.parseDouble(s) * DollarPrice;
                        sinr = p + "";
                        tinr.setText(p + " Rs");


                }




            }else
            if(swith.contains("Rs")){

sinr=swith.replace("Rs"," ").trim();
                tinr.setText(swith);
            }else {
                tinr.setText(swith);
            }


        }


        System.out.println(swith+" "+i.getStringExtra("price")+"  "+swithot);

        theading.setText(shead);
        tdesc.setText(sdesc);
        if(simage!=null)
        Glide.with(this)
                .load(simage).fitCenter().override(1000,1000).into(imgfin);


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        String paymentId = paymentData.getPaymentId();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("ammount",""+sinr);
        hashMap.put("image",simage);
        hashMap.put("location","");
        hashMap.put("other","");
        hashMap.put("paymentid",paymentId);
        hashMap.put("time",""+formatter.format(date));
        hashMap.put("title",shead);

        String id=""+java.time.LocalDate.now()+"_"+sid;

        FirebaseDatabase.getInstance().getReference().child("Payment").child(mail).child("Certification").child(id).setValue(hashMap);

        Intent i= new Intent(this,MyCourseActivity.class);

        startActivity(i);

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }

    public void intents(View view){
        apply.setVisibility(View.VISIBLE);
        arr.setVisibility(View.GONE);
        arrless.setVisibility(View.VISIBLE);
        arrless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply.setVisibility(View.GONE);
                arrless.setVisibility(View.GONE);
                arr.setVisibility(View.VISIBLE);
            }
        });
        final String dit1="10",dit2="20";
        addc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean coup=false;
                Toast.makeText(SingleActivity.this,"Coupon Processing",Toast.LENGTH_SHORT).show();

                if(coupon.getText().toString().isEmpty())
                {
                    coupon.setError("Invalid Code");
                    coup=false;

                }
                if(coupon.getText().toString().toLowerCase().equals("first"))
                {
                    discount.setText(dit1+"%");
                    coup=true;
                   // Toast.makeText(SingleActivity.this,"Coupon Applied",Toast.LENGTH_SHORT).show();
                }
                if(coupon.getText().toString().toLowerCase().equals("festival"))
                {
                    discount.setText(dit2+"%");
                    coup=true;
                }
                if(coup) {
                    apply.setVisibility(View.GONE);
                    arrless.setVisibility(View.GONE);
                    arr.setVisibility(View.VISIBLE);
                    dis.setVisibility(View.VISIBLE);
                    red.setVisibility(View.VISIBLE);
                    real.setVisibility(View.VISIBLE);
                    String dto = discount.getText().toString();
                    int dt = Integer.parseInt(dto.substring(0, dto.indexOf("%")));
                    String pri = twith.getText().toString();
                    double orgi = Double.parseDouble(pri.substring(0, pri.indexOf("$")));
                    double reduction = (orgi * dt) / 100;
                    rp.setText("-" + reduction);
                    double dp = orgi - reduction;

                    op.setText(String.format("%.2f", dp) + "$");
                    double p = dp * DollarPrice;
                    String rs = tinr.getText().toString();
                    rs = rs.substring(0, rs.indexOf(" "));
                    if (rs.contains("."))
                        rs = rs.substring(0, rs.indexOf("."));
                    int rup = Integer.parseInt(rs);
                    ValueAnimator animator = new ValueAnimator();
                    animator.setObjectValues(rup, (int) p);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            tinr.setText(String.valueOf(animation.getAnimatedValue()) + " Rs");
                        }
                    });
                    animator.setEvaluator(new TypeEvaluator<Integer>() {
                        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                            return Math.round(startValue + (endValue - startValue) * fraction);
                        }
                    });
                    animator.setDuration(2000);
                    animator.start();
                    //  int inr=Integer.parseInt(rs.substring(0,rs.indexOf(".")))+1;
                    //  for(int i=inr;i<=p;i++)
                    //{
                    // tinr.setText((int)p+" Rs");
                    //}
                    Toast.makeText(SingleActivity.this, "Coupon Activated", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }
}
