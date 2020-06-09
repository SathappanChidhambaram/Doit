package com.doit.doitappfin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.ProfileData;
import com.doit.doitappfin.utils.myCourseAdapter;
import com.doit.doitappfin.utils.myCourseModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCourseActivity extends AppCompatActivity {
    DatabaseReference reference;

    private RecyclerView recyclerViewMycourse;
    private myCourseAdapter  mydapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    String mail="",num="",oiscomp="";
    ArrayList<myCourseModel> model;

    ArrayList<myCourseModel>list;
    ArrayList<String> date,course;
    myCourseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshRecyclerList;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        list =new ArrayList<myCourseModel>();
        date= new ArrayList<>();
        course =new ArrayList<>();

        getdata();






        recyclerViewMycourse=findViewById(R.id.myRecycler);
        toolbar = (Toolbar) findViewById(R.id.toolbarmycourse);
        //   swipeRefreshRecyclerList = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_recycler_list);
        initToolbar("my course");
        recyclerViewMycourse.setLayoutManager(new LinearLayoutManager(this));


        if(mAuth.getCurrentUser() == null){
            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        }
        acct = GoogleSignIn.getLastSignedInAccount(this);
        getdata();


        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }


    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
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
                    volley();
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

    private void volley() {



        reference = FirebaseDatabase.getInstance().getReference().child("Payment").child(mail).child("Certification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    myCourseModel p =dataSnapshot1.getValue(myCourseModel.class);
                    list.add(p);
                    date.add(dataSnapshot1.getKey());
                    course.add(dataSnapshot1.getKey());



                }

                FirebaseDatabase.getInstance().getReference().child("Payment").child(mail).child("Training").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot dataSnapshot11:dataSnapshot.getChildren()) {
                            myCourseModel p1 = dataSnapshot11.getValue(myCourseModel.class);
                            list.add(p1);
                            date.add(dataSnapshot11.getKey());
                            course.add(dataSnapshot11.getKey());
                        }


                        if(list.size()<1)
                        {
                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "No course available", Snackbar.LENGTH_LONG)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();
                        }
                            adapter = new myCourseAdapter(MyCourseActivity.this, list, date, course);
                            recyclerViewMycourse.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"oopss........Something went wrong",Toast.LENGTH_SHORT).show();

            }
        });



        //   setAdapter();

    }

    private void setAdapter() {

        mydapter=new myCourseAdapter(this,model,date,course);
        recyclerViewMycourse.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMycourse.setAdapter(mydapter);

        mydapter.SetOnItemClickListener(new myCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, myCourseModel model) {

                //handle item click events here




            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getdata() {

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
                                mail=d1.getKey().toString().trim();

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
}
